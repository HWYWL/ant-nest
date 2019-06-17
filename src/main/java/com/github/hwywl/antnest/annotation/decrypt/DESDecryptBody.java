package com.github.hwywl.antnest.annotation.decrypt;

import java.lang.annotation.*;

/**
 * @author YI
 * @version 2019年6月16日
 * @see DESDecryptBody
 */
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DESDecryptBody {

    String otherKey() default "";

}
