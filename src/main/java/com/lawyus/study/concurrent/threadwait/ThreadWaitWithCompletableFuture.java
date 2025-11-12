package com.lawyus.study.concurrent.threadwait;

import java.util.concurrent.CompletableFuture;

public class ThreadWaitWithCompletableFuture {
    public static void main(String[] args) {
        CompletableFuture<Void> threadAFuture = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("thread a: " + i);
            }
        });

        CompletableFuture<Void> threadBFuture = threadAFuture.thenRun(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("thread b: " + i);
            }
        });

        // 等待两个任务都完成
        CompletableFuture.allOf(threadAFuture, threadBFuture).join();
    }
}

