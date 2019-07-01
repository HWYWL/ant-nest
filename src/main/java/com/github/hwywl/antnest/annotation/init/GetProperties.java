package com.github.hwywl.antnest.annotation.init;

import com.github.hwywl.antnest.listener.GetPropertiesListener;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 获取Properties配置文件
 * @author YI
 * @date 2019-6-30
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Import({GetPropertiesListener.class})
public @interface GetProperties {
    /**
     * Properties文件路径
     * @return
     */
    String[] properties();
}
