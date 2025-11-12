package com.lawyus.study.concurrent.rotateprint;

import java.util.concurrent.Semaphore;

/**
 * 两个线程轮流打印数据
 * 信号量实现版
 */
public class RotatePrinterSemaphore {
    private final Semaphore numberSemaphore = new Semaphore(1);
    private final Semaphore letterSemaphore = new Semaphore(0);

    public void printNumbers() {
        try {
            for (int i = 1; i <= 52; i++) {
                numberSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " " + i);
                if (i % 2 == 0) {
                    letterSemaphore.release();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void printLetters() {
        try {
            for (int i = 0; i < 26; i++) {
                letterSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " " + (char)('A' + i));
                numberSemaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void start() {
        RotatePrinterSemaphore printer = new RotatePrinterSemaphore();
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

