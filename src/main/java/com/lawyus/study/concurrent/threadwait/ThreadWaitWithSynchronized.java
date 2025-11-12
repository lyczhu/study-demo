package com.lawyus.study.concurrent.threadwait;

public class ThreadWaitWithSynchronized {
    private static final Object lock = new Object();
    private static boolean threadACompleted = false;

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("thread a: " + i);
                }
                threadACompleted = true;
                lock.notifyAll(); // 通知等待的线程
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (lock) {
                while (!threadACompleted) {
                    try {
                        lock.wait(); // 等待threadA完成
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("thread b: " + i);
                }
            }
        });

        threadA.start();
        threadB.start();
    }
}

