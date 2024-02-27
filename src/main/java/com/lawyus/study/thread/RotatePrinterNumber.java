package com.lawyus.study.thread;

/**
 * 两个线程轮流打印到100
 *
 * @author: lyc
 * @date: 2023/12/17
 */
public class RotatePrinterNumber {
    private volatile int value = 1;
    private boolean flag = true;

    public synchronized void oddPrint() {
        for (int i = 0; i < 50; i++) {
            while (flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName()  + " " +  value++);
            flag = !flag;
            notify();
        }
    }

    public synchronized void evenPrint() {
        for (int i = 0; i < 50; i++) {
            while (!flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + value++);
            flag = !flag;
            notify();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RotatePrinterNumber roratePrinterNumber = new RotatePrinterNumber();
        Thread t1 = new Thread(roratePrinterNumber::oddPrint);
        t1.setName("t1");
        Thread t2 = new Thread(roratePrinterNumber::evenPrint);
        t2.setName("t2");
        t1.start();
        t2.start();

    }
}
