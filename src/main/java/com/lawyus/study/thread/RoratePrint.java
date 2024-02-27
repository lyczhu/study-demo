package com.lawyus.study.thread;

/**
 * 两个线程轮流打印数据
 *
 * @author: lyc
 * @date: 2023/12/16
 */
public class RoratePrint {

    public static void main(String[] args) throws InterruptedException {
        RoratePrint roratePrint = new RoratePrint();
        Thread threadA = new Thread(new RunnableA(roratePrint));
        Thread threadB = new Thread(new RunnableB(roratePrint));
        threadA.start();
        // 确保线程a先执行
        Thread.sleep(100);
        threadB.start();
    }
}

class RunnableA implements Runnable {
    private final RoratePrint roratePrint;

    public RunnableA(RoratePrint roratePrint) {
        this.roratePrint = roratePrint;
    }


    @Override
    public void run() {
        synchronized (roratePrint) {
            for (int i = 0; i < 10; i++) {
                try {
                    roratePrint.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("thread a");

                roratePrint.notify();
            }
        }
    }
}

class RunnableB implements Runnable {

    private final RoratePrint roratePrint;

    public RunnableB(RoratePrint roratePrint) {
        this.roratePrint = roratePrint;
    }

    @Override
    public void run() {

        synchronized (roratePrint) {
            for (int i = 0; i < 10; i++) {
                roratePrint.notify();

                System.out.println("thread b");

                try {
                    roratePrint.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
