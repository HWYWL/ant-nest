package com.github.hwywl.antnest.annotation.web;

import java.lang.annotation.*;

/**
 * 注解apo拦截
 *
 * @author YI
 * @date 2019年4月29日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
    /**
     * 接口描述
     * @return
     */
    String description() default "";
}
