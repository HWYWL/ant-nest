package com.github.hwywl.antnest.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件读取
 * @author YI
 * @date 2019-5-5
 */
@Component
@ConfigurationProperties(prefix = "current.limiting")
public class CurrentProperties {
    /**
     * 打开限流功能
     */
    private boolean enabled = false;

    /**
     * 使限流注解的作用失效
     */
    private boolean partEnabled = true;

    /**
     * 回收过期的限制器对象需要的时间(秒)
     */
    private long recycling = 10;

    /**
     * 执行计划任务的线程池中的核心线程数
     */
    private int corePoolSize = 10;

    public boolean isEnabled() {
        return enabled;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPartEnabled() {
        return partEnabled;
    }

    public void setPartEnabled(boolean partEnabled) {
        this.partEnabled = partEnabled;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public long getRecycling() {
        return recycling;
    }

    public void setRecycling(long recycling) {
        this.recycling = recycling;
    }
}
