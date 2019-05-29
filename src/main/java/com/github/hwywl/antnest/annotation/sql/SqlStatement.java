package com.github.hwywl.antnest.annotation.sql;

import java.lang.annotation.*;

/**
 * SQL拦截
 *
 * @author YI
 * @date 2019-5-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SqlStatement {
    /**
     * SQL描述
     * @return
     */
    String description() default "";

    /**
     * 创建人
     * @return
     */
    String author() default "";
}
