package com.github.hwywl.antnest.annotation.web;

import java.lang.annotation.*;

/**
 * 计算方法或者请求接口的耗时
 *
 * @author YI
 * @date 2019-6-15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OperatingTime {
    /**
     * 接口描述
     * @return
     */
    String description() default "";
}
