package com.lawyus.study.concurrent.producerconsumer;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 用阻塞队列模拟生产者消费者
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        BlockingQueue bq = new BlockingQueue(1);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    bq.put(i + "");
                    System.out.println("生产数据：" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String s = bq.get();
                    System.out.println("消费数据：" + s);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        consumer.start();
    }
}

@Slf4j
class BlockingQueue {

    private final List<String> queue;
    private final int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayList<>(capacity);
    }

    public synchronized void put(String s) throws InterruptedException {
        while (queue.size() <= capacity) {
            wait();
        }
        queue.add(s);
        notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        String s = queue.removeLast();
        notifyAll();

        return s;
    }
}
