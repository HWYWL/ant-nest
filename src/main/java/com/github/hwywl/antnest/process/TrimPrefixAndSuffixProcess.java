package com.github.hwywl.antnest.process;

import com.github.hwywl.antnest.annotation.process.TrimPrefixAndSuffix;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 去除前后的空格
 *
 * @author YI
 * @date 2019年5月22日
 */
public class TrimPrefixAndSuffixProcess extends AbstractProcess{

    @Override
    protected Annotation getAnnotation(Field field) {
        return field.getAnnotation(TrimPrefixAndSuffix.class);
    }

    @Override
    protected String getProcessResult(String src) {
        return src.trim();
    }
}
