package com.lawyus.study.concurrent.threadwait;

import java.util.concurrent.CountDownLatch;

public class ThreadWaitWithCountDownLatch {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);

        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("thread a: " + i);
            }
            latch.countDown(); // 计数减1，表示完成
        });

        Thread threadB = new Thread(() -> {
            try {
                latch.await(); // 等待计数变为0
                for (int i = 0; i < 10; i++) {
                    System.out.println("thread b: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadA.start();
        threadB.start();
    }
}
