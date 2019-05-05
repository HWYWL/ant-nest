package com.github.hwywl.antnest.handler;

import com.github.hwywl.antnest.annotation.CurrentLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 拦截反馈
 *
 * @author YI
 * @date 2019-5-5
 */
public interface CurrentAspectHandler {
    /**
     * CurrentLimiter注解拦截后的反馈
     */
    Object around(ProceedingJoinPoint pjp, CurrentLimiter rateLimiter)throws Throwable;
}
