package com.lawyus.study.concurrent.producerconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumerWithStandardQueue {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    queue.put(i + "");
                    System.out.println("生产数据：" + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String s = queue.take();
                    System.out.println("消费数据：" + s);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        producer.start();
        consumer.start();
    }
}

