package com.github.hwywl.antnest.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.github.hwywl.antnest.annotation.locak.LockKeyParam;
import com.github.hwywl.antnest.annotation.locak.ZooLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 使用 aop 切面实现分布式锁
 *
 * @author YI
 * @date 2019-7-3
 */
@Aspect
@Component
@Slf4j
public class ZooLockAspect {
    private final CuratorFramework zkClient;

    private static final String KEY_PREFIX = "DISTRIBUTED_LOCK_";

    @Autowired
    public ZooLockAspect(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.locak.ZooLock)")
    public void doLock() {

    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("doLock()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();

        ZooLock zooLock = method.getAnnotation(ZooLock.class);

        if (StrUtil.isBlank(zooLock.key())) {
            throw new RuntimeException("分布式锁键不能为空");
        }

        String lockKey = buildLockKey(zooLock, method, args);
        InterProcessMutex lock = new InterProcessMutex(zkClient, lockKey);

        try {
            // 假设上锁成功，以后拿到的都是 false
            if (lock.acquire(zooLock.timeout(), zooLock.timeUnit())) {
                return point.proceed();
            } else {
                throw new RuntimeException("请勿重复提交");
            }
        } finally {
            lock.release();
        }
    }

    /**
     * 构造分布式锁的键
     *
     * @param lock   注解
     * @param method 注解标记的方法
     * @param args   方法上的参数
     * @return 分布式锁键
     */
    private String buildLockKey(ZooLock lock, Method method, Object[] args) {
        StringBuilder key = new StringBuilder(StrUtil.SLASH + KEY_PREFIX + lock.key());

        // 迭代全部参数的注解，根据使用LockKeyParam的注解的参数所在的下标，来获取args中对应下标的参数值拼接到前半部分key上
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 循环该参数全部注解
            for (Annotation annotation : parameterAnnotations[i]) {
                // 注解不是 @LockKeyParam
                if (!(annotation instanceof LockKeyParam)) {
                    continue;
                }

                // 获取所有fields
                String[] fields = ((LockKeyParam) annotation).fields();
                if (ArrayUtil.isEmpty(fields)) {
                    // 普通数据类型直接拼接
                    if (ObjectUtil.isNull(args[i])) {
                        throw new RuntimeException("动态参数不能为null");
                    }
                    key.append(StrUtil.SLASH).append(args[i]);
                } else {
                    // @LockKeyParam的fields值不为null，所以当前参数应该是对象类型
                    for (String field : fields) {
                        JSON parse = JSONUtil.parse(args[i]);
                        Object value = parse.getByPath(field);
                        if (ObjectUtil.isNull(value)){
                            throw new RuntimeException("动态参数不能为null");
                        }

                        key.append(StrUtil.SLASH).append(value);
                    }
                }
            }
        }

        return key.toString();
    }

}
