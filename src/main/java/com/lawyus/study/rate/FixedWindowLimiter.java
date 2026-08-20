package com.lawyus.study.rate;

public class FixedWindowLimiter {
    private final int maxRequests;      // 窗口内最大请求数
    private final long windowSize;      // 窗口大小（毫秒）
    private int counter;               // 当前请求计数
    private long lastResetTime;        // 上次重置时间

    public FixedWindowLimiter(int maxRequests, long windowSize) {
        this.maxRequests = maxRequests;
        this.windowSize = windowSize;
        this.counter = 0;
        this.lastResetTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();

        // 检查是否需要重置窗口
        if (now - lastResetTime >= windowSize) {
            counter = 0;
            lastResetTime = now;
        }

        // 判断是否超过限制
        if (counter < maxRequests) {
            counter++;
            return true;
        }
        return false;
    }
}
