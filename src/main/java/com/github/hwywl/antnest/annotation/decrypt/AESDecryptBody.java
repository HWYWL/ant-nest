package com.github.hwywl.antnest.annotation.decrypt;

import java.lang.annotation.*;

/**
 * @author YI
 * @version 2019年6月16日
 * @see AESDecryptBody
 */
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AESDecryptBody {

    String otherKey() default "";

}
