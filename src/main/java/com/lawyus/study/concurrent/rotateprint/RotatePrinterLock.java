package com.lawyus.study.concurrent.rotateprint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 两个线程轮流打印数据
 * 重入锁实现版
 */
public class RotatePrinterLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition numberCondition = lock.newCondition();
    private final Condition letterCondition = lock.newCondition();
    private boolean printNumber = true;

    public void printNumbers() {
        lock.lock();
        try {
            for (int i = 1; i <= 52; i++) {
                while (!printNumber) {
                    numberCondition.await();
                }
                System.out.println(Thread.currentThread().getName() + " " + i);
                if (i % 2 == 0) {
                    printNumber = false;
                    letterCondition.signal();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void printLetters() {
        lock.lock();
        try {
            for (int i = 0; i < 26; i++) {
                while (printNumber) {
                    letterCondition.await();
                }
                System.out.println(Thread.currentThread().getName() + " " + (char)('A' + i));
                printNumber = true;
                numberCondition.signal();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    static void start() {
        RotatePrinterLock printer = new RotatePrinterLock();
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

