package com.github.hwywl.antnest.interceptor;

import com.github.hwywl.antnest.annotation.sql.SqlStatement;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

/**
 * sql拦截
 * @author YI
 * @date 2019-5-29
 */
@Slf4j
@Aspect
@Component
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class SqlStatementInterceptor implements Interceptor {
    private ConcurrentHashMap description;
    /**
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.sql.SqlStatement)")
    public void sqlPointCut() {}

    @Before("sqlPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        description = getAspectLogDescription(joinPoint);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (description == null || description.isEmpty()){
            return invocation.proceed();
        }

        Object returnValue;
        long start = System.currentTimeMillis();
        // 执行 SQL语句
        returnValue = invocation.proceed();
        long end = System.currentTimeMillis();

        try {
            final Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = null;
            //获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
            if (args.length > 1) {
                parameter = invocation.getArgs()[1];
            }
            // 获取到节点的 id,即 sql语句的 id
            String sqlId = ms.getId();
            // BoundSql就是封装 MyBatis最终产生的 sql类
            BoundSql boundSql = ms.getBoundSql(parameter);
            // 获取节点的配置
            Configuration configuration = ms.getConfiguration();
            // 获取到最终的 sql语句
            printSql(configuration, boundSql, sqlId, end - start, description);
        } catch (Exception e) {
            log.error("sql拦截异常:{} ", e.getMessage());
        }

        return returnValue;
    }

    /**
     * 获取到最终的 sql语句
     * @param configuration 获取节点的配置
     * @param boundSql BoundSql就是封装 MyBatis最终产生的 sql类
     * @param sqlId 获取到节点的 id,即 sql语句的 id
     * @param time 耗时
     * @param description 用户编写的注解
     */
    private void printSql(Configuration configuration, BoundSql boundSql, String sqlId, long time, Map description) {
        String sql = showSql(configuration, boundSql);
        log.info("【SQL接口描述】>>>> 作者：{} >>>> 接口用途：{}", description.get("author"), description.get("description"));
        log.info("【SQL语句Id】>>>> {}", sqlId);
        log.info("【SQL语句耗时】>>>> {} ms", time);
        log.info("【SQL语句】>>>> {}", sql);
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (!CollectionUtils.isEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换　　　　　　
            // 如果根据 parameterObject.getClass(）可以找到对应的类型，则替换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                // MetaObject主要是封装了 originalObject对象，提供了 get和 set的方法用于获取和设置 originalObject的属性值
                // 主要支持对 JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();

                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        // 该分支是动态 sql
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }

        return sql;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // do nothing
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public ConcurrentHashMap getAspectLogDescription(JoinPoint joinPoint) throws Exception {
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);

        Method[] methods = targetClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    if (method.isAnnotationPresent(SqlStatement.class)) {
                        SqlStatement methodAnno = method.getAnnotation(SqlStatement.class);
                        map.put("author", methodAnno.author());
                        map.put("description", methodAnno.description());
                    }
                }
            }
        }

        return map;
    }
}

