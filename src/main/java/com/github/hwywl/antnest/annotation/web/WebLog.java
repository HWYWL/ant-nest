package com.github.hwywl.antnest.annotation.web;

import java.lang.annotation.*;

/**
 * 注解apo拦截日志
 *
 * @author YI
 * @date 2019年4月29日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
    /**
     * 方法描述
     *
     * @return
     */
    String description() default "";
}
