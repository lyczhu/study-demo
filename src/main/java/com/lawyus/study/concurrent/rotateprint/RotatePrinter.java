package com.lawyus.study.concurrent.rotateprint;

/**
 * 两个线程轮流打印数据
 * (原始版)
 */
public class RotatePrinter {
    private boolean printNumber = true;

    public synchronized void printNumbers() {
        for (int i = 0; i < 52;) {
            while (!printNumber) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName()  + " " +  ++i);
            System.out.println(Thread.currentThread().getName()  + " " +  ++i);
            printNumber = !printNumber;
            notifyAll();
        }
    }

    public synchronized void printLetters() {
        for (int i = 0; i < 26; i++) {
            while (printNumber) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + ((char) (65 + i)));
            printNumber = !printNumber;
            notifyAll();
        }
    }

    static void start() {
        RotatePrinter printer = new RotatePrinter();
        Thread nThread = new Thread(printer::printNumbers);
        nThread.setName("nThread: ");
        Thread lThread = new Thread(printer::printLetters);
        lThread.setName("lThread: ");
        nThread.start();
        lThread.start();
    }

    public static void main(String[] args) {
        start();
    }
}
