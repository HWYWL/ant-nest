package com.github.hwywl.antnest.annotation.process;

import java.lang.annotation.*;

/**
 * 给字符串trim，前后空格都去掉
 * 字段非null时生效
 *
 * @author YI
 * @date 2019年5月22日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TrimPrefixAndSuffix {

}
