package com.github.hwywl.antnest.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Zookeeper 配置项
 *
 * @author YI
 * @date 2019-7-3
 */
@Data
@ConfigurationProperties(prefix = "zk.lock")
public class ZkProps {
    /**
     * 是否启用zookeeper分布式锁
     */
    private boolean enabled = false;

    /**
     * 连接地址
     */
    private String url = "127.0.0.1:2181";

    /**
     * 超时时间(毫秒)，默认1000
     */
    private int timeout = 1000;

    /**
     * 重试次数，默认3
     */
    private int retry = 3;
}
