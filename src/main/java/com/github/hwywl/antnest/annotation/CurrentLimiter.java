package com.github.hwywl.antnest.annotation;

import java.lang.annotation.*;

/**
 * 限流
 *
 * @author YI
 * @date 2019-5-5 15:25:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface CurrentLimiter {
    /**
     * 允许访问的次数，默认值20
     */
    double QPS() default 20;

    /**
     * 时间段，单位为毫秒，默认值一分钟
     */
    long initialDelay() default 0;

    /**
     * 阻塞策略
     */
    boolean failFast() default true;

    /**
     * 切换为漏桶算法,true为切换
     */
    boolean overflow() default true;
}
