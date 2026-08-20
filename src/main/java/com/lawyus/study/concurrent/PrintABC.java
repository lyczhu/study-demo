package com.lawyus.study.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintABC {
    private int flag = 0; // 0:A,1:B,2:C
    private final Lock lock = new ReentrantLock();
    private final Condition c1 = lock.newCondition();
    private final Condition c2 = lock.newCondition();
    private final Condition c3 = lock.newCondition();

    public void printA() {
        lock.lock();
        try {
            while (flag != 0) c1.await();
            System.out.print("A");
            flag = 1;
            c2.signal();
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }
    // B、C 逻辑同理，修改flag和唤醒对象即可

    public void printB() {
        lock.lock();
        try {
            while (flag != 1) c2.await();
            System.out.print("B");
            flag = 2;
            c3.signal(); // 唤醒C
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public void printC() {
        lock.lock();
        try {
            while (flag != 2) c3.await();
            System.out.print("C");
            flag = 0;
            c1.signal(); // 唤醒A
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        PrintABC printABC = new PrintABC();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                printABC.printB();
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                printABC.printA();
            }
        }).start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                printABC.printC();
            }
        }).start();

/*        while (true) {
            if (Thread.activeCount() <= 1) break;
        }*/
    }
}
