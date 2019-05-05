package com.github.hwywl.antnest.core;

import com.github.hwywl.antnest.utils.SpringContextUtil;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 令牌桶算法实现纳秒级流控接口
 *
 * @author YI
 * @date 2019-5-5
 */
public interface RateLimiter {
    String message = "当前服务不可用!!!";

    ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(SpringContextUtil.getCorePoolSize());

    /**
     * CAS获取令牌,阻塞直到成功
     * @return
     */
    boolean tryAcquire();

    /**
     * CAS获取令牌,没有令牌立即失败
     * @return
     */
    boolean tryAcquireFailed();

    LocalDateTime getExpirationTime();
}
