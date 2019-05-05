package com.github.hwywl.antnest.utils;

import com.github.hwywl.antnest.properties.CurrentProperties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring框架上下文切换
 * @author YI
 * @date 2019-5-5
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;

    private static String applicationName;
    private static String port;
    private static int corePoolSize;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        SpringContextUtil.applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        SpringContextUtil.applicationName = SpringContextUtil.applicationName == null ? "application" : SpringContextUtil.applicationName;
        SpringContextUtil.port = applicationContext.getEnvironment().getProperty("server.port");
        SpringContextUtil.port = SpringContextUtil.port == null ? "8080" : SpringContextUtil.port;
        SpringContextUtil.corePoolSize = applicationContext.getBean(CurrentProperties.class).getCorePoolSize();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static String getPort() {
        return port;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static int getCorePoolSize() {
        return corePoolSize;
    }

    public static <T> T getBean(Class<?> clz) throws BeansException {
        return (T) applicationContext.getBean(clz);
    }
}
