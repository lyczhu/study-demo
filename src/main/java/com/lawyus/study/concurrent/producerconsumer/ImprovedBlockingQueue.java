package com.lawyus.study.concurrent.producerconsumer;

import java.util.ArrayList;
import java.util.List;

class ImprovedBlockingQueue {
    private final List<String> queue;
    private final int capacity;

    public ImprovedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayList<>(capacity);
    }

    public synchronized void put(String s) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait(); // 队列满时等待
        }
        queue.add(s);
        notifyAll(); // 通知等待的消费者
    }

    public synchronized String get() throws InterruptedException {
        while (queue.isEmpty()) {
            wait(); // 队列空时等待
        }
        String s = queue.removeFirst(); // FIFO，从头部取出
        notifyAll(); // 通知等待的生产者
        return s;
    }
}

