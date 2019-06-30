package com.github.hwywl.antnest.annotation.web;

/**
 * 方法计数器
 *
 * @author YI
 * @date 2019-6-30 13:51:41
 */
public @interface MethodCounter {
    /**
     * 方法描述
     *
     * @return
     */
    String description() default "";
}
