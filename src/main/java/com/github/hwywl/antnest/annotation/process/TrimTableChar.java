package com.github.hwywl.antnest.annotation.process;

import java.lang.annotation.*;

/**
 * 移除字符串的制表符,例如，\r\n等
 * 非null时生效
 *
 * @author YI
 * @date 2019年5月22日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TrimTableChar {
}
