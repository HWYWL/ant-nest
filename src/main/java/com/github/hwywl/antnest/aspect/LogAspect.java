package com.github.hwywl.antnest.aspect;

import com.alibaba.fastjson.JSON;
import com.github.hwywl.antnest.annotation.web.WebLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 请求拦截
 *
 * @author YI
 * @date 2018-7-19
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.web.WebLog)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Map<String, String[]> parameterMap = request.getParameterMap();
        // 记录下请求内容
        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        log.info("请求地址 : " + request.getRequestURL().toString());
        log.info("接口描述 : " + getAspectLogDescription(joinPoint));
        log.info("HTTP METHOD : " + request.getMethod());
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
        log.info("请求参数 : " + JSON.toJSONString(parameterMap));
    }

    /**
     * returning的值和doAfterReturning的参数名一致
     *
     * @param ret
     * @throws Throwable
     */
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        log.info("返回值 : " + ret);
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After("logPointCut()")
    public void doAfter() throws Throwable {
        // 接口结束后换行，方便分割查看
        log.info("=========================================== End ===========================================");
    }

    /**
     * 环绕通知,环绕增强
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        // ob 为方法的返回值
        Object ob = pjp.proceed();
        log.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);

        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(WebLog.class).description());
                    break;
                }
            }
        }

        return description.toString();
    }
}
