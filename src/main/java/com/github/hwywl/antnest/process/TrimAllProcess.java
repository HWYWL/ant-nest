package com.github.hwywl.antnest.process;

import com.github.hwywl.antnest.annotation.process.TrimAll;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 去掉所有空格，前面，后面，和中间
 *
 * @author YI
 * @date 2019年5月22日
 */
public class TrimAllProcess extends AbstractProcess{

    @Override
    protected Annotation getAnnotation(Field field) {
        return field.getAnnotation(TrimAll.class);
    }

    @Override
    protected String getProcessResult(String src) {
        return src.replaceAll(" ","");

    }
}
