package com.lawyus.study.concurrent.rotateprint;

/**
 * 两个线程轮流打印数据
 * 改进版
 */
public class RotatePrinter {
    private boolean printNumber = true;

    public synchronized void printNumbers() {
        for (int i = 1; i <= 52; i++) {
            while (!printNumber) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
            if (i % 2 == 0) {
                printNumber = false;
                notifyAll();
            }
        }
    }

    public synchronized void printLetters() {
        for (int i = 0; i < 26; i++) {
            while (printNumber) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println(Thread.currentThread().getName() + " " + (char)('A' + i));
            printNumber = true;
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

    static void main(String[] args) {
        start();
    }
}
