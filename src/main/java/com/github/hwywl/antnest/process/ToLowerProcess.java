package com.github.hwywl.antnest.process;

import com.github.hwywl.antnest.annotation.process.ToLower;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 转小写
 *
 * @author YI
 * @date 2019年5月22日
 */
public class ToLowerProcess extends AbstractProcess{

    @Override
    protected Annotation getAnnotation(Field field) {
        return field.getAnnotation(ToLower.class);
    }

    @Override
    protected String getProcessResult(String src) {
        return src.toLowerCase();
    }
}
