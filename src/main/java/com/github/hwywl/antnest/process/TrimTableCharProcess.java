package com.github.hwywl.antnest.process;

import com.github.hwywl.antnest.annotation.process.TrimTableChar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 去除制表符
 *
 * @author YI
 * @date 2019年5月22日
 */
public class TrimTableCharProcess extends AbstractProcess{
    private static final Pattern P = Pattern.compile("\\s*|\t|\r|\n");

    @Override
    protected Annotation getAnnotation(Field field) {
        return field.getAnnotation(TrimTableChar.class);
    }

    @Override
    protected String getProcessResult(String src) {
        String repl = "";
        if (src!=null) {
            Matcher m = P.matcher(src);
            repl = m.replaceAll("");
        }
        repl= repl.replaceAll("\\\\r","");
        repl= repl.replaceAll("\\\\n","");
        repl= repl.replaceAll("\\\\r\\\\n","");
        return repl;
    }
}
