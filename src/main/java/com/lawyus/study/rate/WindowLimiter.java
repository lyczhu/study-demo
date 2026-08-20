package com.lawyus.study.rate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowLimiter {

    private static final int WINDOW_SIZE = 1000;

    private static final int THRESHOLD = 10;

    private volatile long lastTimeStart = System.currentTimeMillis();

    private final AtomicInteger count = new AtomicInteger(0);

    public static Map<Long, Integer> countMap = new ConcurrentHashMap<>();

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();

        if (now - lastTimeStart >= WINDOW_SIZE) {
            if (count.compareAndSet(count.get(), 0)) {
                lastTimeStart = now;
            }
        }

        if (count.get() < THRESHOLD) {
            int value = count.incrementAndGet();
            countMap.put(now / 1000, value);
            return value <= THRESHOLD;
        }

        return false;
    }

}
