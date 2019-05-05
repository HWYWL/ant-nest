package com.github.hwywl.antnest.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶算法实现纳秒级流控
 * @author YI
 * @date 2019-5-5
 */
public class RateLimiterSingle implements RateLimiter {
    /**
     * 令牌桶容量
     */
    private long size;
    /**
     * 间隔：纳秒
     */
    private long period;
    /**
     * 延迟生效时间：毫秒
     */
    private long initialDelay;
    /**
     * 令牌桶初始容量：0
     */
    private AtomicLong bucket = new AtomicLong(0);
    /**
     * 限流器对象到期时间
     */
    private LocalDateTime ExpirationTime;

    /**
     * @param QPS          每秒并发量,等于0 默认禁止访问
     * @param initialDelay 首次延迟时间：毫秒
     * @param overflow     是否严格控制请求速率和次数
     */
    private RateLimiterSingle(double QPS, long initialDelay, boolean overflow) {
        this.size = overflow ? 1 : (QPS < 1 ? 1 : new Double(QPS).longValue());
        //毫秒转纳秒
        this.initialDelay = initialDelay * 1000 * 1000;
        this.period = QPS != 0 ? new Double(1000 * 1000 * 1000 / QPS).longValue() : Integer.MAX_VALUE;
        //等于0就不放令牌了
        if (QPS != 0) {
            putScheduled();
        }
    }

    public static RateLimiter of(double QPS, long initialDelay, boolean overflow) {
        return new RateLimiterSingle(QPS, initialDelay, overflow);
    }

    public static RateLimiter of(double QPS, long initialDelay, boolean overflow, long time, ChronoUnit unit) {
        RateLimiterSingle rateLimiterSingle = new RateLimiterSingle(QPS, initialDelay, overflow);
        if (unit != null) {
            LocalDateTime localDateTime = LocalDateTime.now().plus(time, unit);
            rateLimiterSingle.setExpirationTime(localDateTime);
        }
        return rateLimiterSingle;
    }

    /**
     * CAS获取令牌,阻塞直到成功
     */
    @Override
    public boolean tryAcquire() {
        long l = bucket.longValue();
        while (!(l > 0 && bucket.compareAndSet(l, l - 1))) {
            l = bucket.longValue();
        }
        return true;
    }

    /**
     * CAS获取令牌,没有令牌立即失败
     */
    @Override
    public boolean tryAcquireFailed() {
        long l = bucket.longValue();
        while (l > 0) {
            if (bucket.compareAndSet(l, l - 1)) {
                return true;
            }
            l = bucket.longValue();
        }
        return false;
    }

    /**
     * 周期性放令牌，控制访问速率
     */
    private void putScheduled() {
        RateLimiter.scheduled.scheduleAtFixedRate(() -> {
            if (size > bucket.longValue()) {
                bucket.incrementAndGet();
            }
        }, initialDelay, period, TimeUnit.NANOSECONDS);
    }

    @Override
    public LocalDateTime getExpirationTime() {
        return ExpirationTime;
    }

    private void setExpirationTime(LocalDateTime expirationTime) {
        ExpirationTime = expirationTime;
    }
}
