package com.github.hwywl.antnest.process;

import com.github.hwywl.antnest.annotation.process.ToUpper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 大写处理类
 *
 * @author YI
 * @date 2019年5月22日
 */
public class ToUpperProcess extends AbstractProcess {

    @Override
    protected Annotation getAnnotation(Field field) {
        return field.getAnnotation(ToUpper.class);
    }

    @Override
    protected String getProcessResult(String src) {
        return src.toUpperCase();
    }
}
