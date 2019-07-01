package com.github.hwywl.antnest.listener;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.github.hwywl.antnest.annotation.init.GetProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取配置文件
 *
 * @author YI
 * @date 2019年6月15日
 */
@Component
public class GetPropertiesListener implements BeanPostProcessor {
    public static final ConcurrentHashMap<String, Object> CACHEMAP = new ConcurrentHashMap<>(64);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // 方法加载
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            for (Method method : methods) {
                GetProperties permissionOperation = AnnotationUtils.findAnnotation(method, GetProperties.class);
                cachePropertiesMap(permissionOperation);
            }
        }

        // 类加载
        GetProperties permissionOperation = AnnotationUtils.findAnnotation(bean.getClass(), GetProperties.class);
        cachePropertiesMap(permissionOperation);

        return bean;
    }

    /**
     * 把配置文件放入缓存
     *
     * @param properties
     */
    private void cachePropertiesMap(GetProperties properties) {
        try {
            if (null != properties) {
                String[] ps = properties.properties();
                for (String p : ps) {
                    Properties prop = Props.getProp(p);
                    String[] split = p.split("properties");
                    String key = split[0];
                    if (StrUtil.containsAny(key, StrUtil.SLASH)) {
                        key = key.replaceAll(StrUtil.SLASH, StrUtil.DOT);
                    }

                    // 数据放入临时缓存
                    for (Object pkage : prop.keySet()) {
                        CACHEMAP.put(key + pkage, prop.get(pkage));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
