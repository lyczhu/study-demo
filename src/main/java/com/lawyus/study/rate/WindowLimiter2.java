package com.lawyus.study.rate;

import java.util.concurrent.atomic.AtomicReference;

public class WindowLimiter2 {
    private static final int WINDOW_SIZE = 1000;
    private static final int THRESHOLD = 10;

    // 定义一个内部类来保存状态
    private record State(long lastTimeStart, int count) {
    }

    private final AtomicReference<State> stateRef = new AtomicReference<>(
            new State(System.currentTimeMillis(), 0)
    );

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();

        while (true) {
            State current = stateRef.get();
            int newCount;
            long newLastTimeStart = current.lastTimeStart;

            // 检查是否需要重置窗口
            if (now - current.lastTimeStart >= WINDOW_SIZE) {
                newLastTimeStart = now;
                newCount = 0;
            } else {
                newCount = current.count;
            }

            // 检查是否超限
            if (newCount >= THRESHOLD) {
                return false;
            }

            // 尝试更新状态：计数 +1，时间戳视情况更新
            State nextState = new State(newLastTimeStart, newCount + 1);

            if (stateRef.compareAndSet(current, nextState)) {
                return true;
            }
            // CAS 失败，重试
        }
    }
}