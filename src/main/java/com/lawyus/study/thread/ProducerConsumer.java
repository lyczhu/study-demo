package com.lawyus.study.thread;

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
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String s = bq.get();
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
        while (true) {
            if (queue.size() < capacity) {
                queue.add(s);
                log.info("生产数据：{}", s);
                notify();
                break;
            } else {
                log.warn("空间不足无法生产");
                wait();
            }
        }
    }

    public synchronized String get() throws InterruptedException {
        String s;
        while (true) {
            if (!queue.isEmpty()) {
                s =  queue.remove(queue.size() - 1);
                log.info("消费数据：{}", s);
                notify();
                break;
            } else {
                log.warn("没有数据可以消费");
                wait();
            }
        }
        return s;
    }
}
