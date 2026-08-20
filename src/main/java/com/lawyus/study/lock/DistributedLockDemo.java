package com.lawyus.study.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class DistributedLockDemo {

    private final RedissonClient redissonClient;

    public DistributedLockDemo(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void lockDemo() {
        RLock rLock = redissonClient.getLock("lock");
        try {
            rLock.lock();
            // 执行业务方法
            business();
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public void lockDemo2() {
        RLock rLock = redissonClient.getLock("lock");
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            // 执行业务方法
            business();
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public void lockDemo3() {
        RLock rLock = redissonClient.getLock("lock");
        try {
            boolean acquire = rLock.tryLock(10, 20, TimeUnit.SECONDS);
            if (acquire) {
                // 执行业务方法
                business();
            } else {
                // 获取锁失败, 降级/重试/提示
                System.out.println("获取锁失败");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    private void business() {

    }
}
