package com.github.hwywl.antnest.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求拦截，耗时计算
 *
 * @author YI
 * @date 2019年6月15日
 */
@Slf4j
@Aspect
@Component
public class MethodCounterAspect {
    public static final ConcurrentHashMap<String, Long> CACHEMAP = new ConcurrentHashMap<>(16);

    /**
     * 两个..代表所有子目录，最后括号里的两个..代表所有参数
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.web.MethodCounter)")
    public void logPointCut() {

    }

    @Before(value = "logPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();

        synchronized (CACHEMAP) {
            Long aLong = CACHEMAP.get(methodName);
            if (aLong == null) {
                CACHEMAP.put(methodName, 1L);
            } else {
                CACHEMAP.put(methodName, aLong + 1L);
            }
        }
    }
}
