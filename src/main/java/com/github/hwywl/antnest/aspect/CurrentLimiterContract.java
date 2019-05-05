package com.github.hwywl.antnest.aspect;

import com.github.hwywl.antnest.annotation.CurrentLimiter;
import com.github.hwywl.antnest.core.RateLimiter;
import com.github.hwywl.antnest.core.RateLimiterSingle;
import com.github.hwywl.antnest.handler.CurrentAspectHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流
 *
 * @author YI
 * @date 2019-5-5
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "current.limiting", name = "part-enabled", havingValue = "true", matchIfMissing = true)
public class CurrentLimiterContract {
    /**
     * 一个方法一个限流器
     */
    private Map<String, RateLimiter> map = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private CurrentAspectHandler handler;

    /**
     * 声明切入点
     */
    @Pointcut("@annotation(com.github.hwywl.antnest.annotation.CurrentLimiter)")
    public void pointcut() {
    }

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     *
     * @param pjp
     * @param currentLimiter
     * @return
     * @throws Throwable
     */
    @Around("pointcut() && @annotation(currentLimiter)")
    public Object around(ProceedingJoinPoint pjp, CurrentLimiter currentLimiter) throws Throwable {
        //初始化限流器
        RateLimiter rateLimiter = initCurrentLimiting(pjp, currentLimiter);
        //执行快速失败
        if (currentLimiter.failFast()) {
            return tryAcquireFailed(pjp, currentLimiter, rateLimiter);
        } else { //执行阻塞策略
            rateLimiter.tryAcquire();

            return pjp.proceed();
        }
    }

    private Object tryAcquireFailed(ProceedingJoinPoint pjp, CurrentLimiter currentLimiter, RateLimiter rateLimiter) throws Throwable {
        //取到令牌
        if (rateLimiter.tryAcquireFailed()) {
            return pjp.proceed();
        } else {
            //没取到令牌
            return handler == null ? RateLimiter.message : handler.around(pjp, currentLimiter);
        }
    }

    /**
     * 初始化限流器
     * 为了提高性能，不加同步锁，所以存在初始的误差。
     */
    private RateLimiter initCurrentLimiting(ProceedingJoinPoint pjp, CurrentLimiter currentLimiter) {
        String key = pjp.getSignature().toLongString();
        if (!map.containsKey(key)) {
            map.put(key, RateLimiterSingle.of(currentLimiter.QPS(), currentLimiter.initialDelay(), currentLimiter.overflow()));
        }

        return map.get(key);
    }
}
