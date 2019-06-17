package com.github.hwywl.antnest.annotation.encrypt;

import java.lang.annotation.*;

/**
 * @author YI
 * @version 2019年6月16日
 * @see AESEncryptBody
 */
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AESEncryptBody {

    String otherKey() default "";

}
