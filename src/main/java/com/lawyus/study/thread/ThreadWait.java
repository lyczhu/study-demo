package com.lawyus.study.thread;

/**
 * 线程a先执行，线程b等待线程a执行完成才开始执行
 *
 * @author: lyc
 * @date: 2023/12/16
 */
public class ThreadWait {
    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("thread a: " + i);
            }
        });
        Thread threadB = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("thread b: " + i);
            }
        });
        threadA.start();
        threadA.join();
        threadB.start();
    }
}
