package com.lawyus.study.concurrent.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumerWithExecutor {

    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {

            // 生产者任务
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        queue.put(i + "");
                        System.out.println("生产数据：" + i);
                        Thread.sleep(100); // 模拟生产时间
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // 消费者任务
            executor.submit(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        String s = queue.take();
                        System.out.println("消费数据：" + s);
                        Thread.sleep(150); // 模拟消费时间
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executor.shutdown();
        }
    }
}
