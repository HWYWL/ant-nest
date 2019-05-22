package com.github.hwywl.antnest.annotation.process;

import java.lang.annotation.*;

/**
 * 将字符串的全部空格都去掉（前面和后面还有中间）
 * 非null时进行
 *
 * @author YI
 * @date 2019年5月22日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TrimAll {
}
