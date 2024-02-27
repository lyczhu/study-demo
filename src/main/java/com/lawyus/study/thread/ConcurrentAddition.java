package com.lawyus.study.thread;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 并发加
 * 用10个线程从1加到100，第1个线程从1加到10，第2个线程从11加到20，...，等10个线程都加完后再求总和
 *
 * @author: lyc
 * @date: 2023/12/17
 */
public class ConcurrentAddition {

    // 若要使用total来计算总值的话，totalIncrement方法需要是同步方法
    /*private volatile int total = 0;

    private synchronized void totalIncrement(int value) {
        total = total + value;
    }*/

    public int addWithInitValue(int initValue) {
        int partTotal = 0;
        for (int i = 0; i < 10; i++) {
            partTotal = partTotal + initValue + i;
        }
        //totalIncrement(partTotal);
        return partTotal;
    }

    public static void main(String[] args) throws InterruptedException {
        countByCountDownLatch();
        //System.out.println(concurrentAddition.total);
    }

    private static void countByCountDownLatch() throws InterruptedException {
        ConcurrentAddition concurrentAddition = new ConcurrentAddition();
        int threadCount = 100;
        CountDownLatch cdl = new CountDownLatch(threadCount);
        int[] arr = new int[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int init = i;
            new Thread(() -> {
                arr[init] = concurrentAddition.addWithInitValue(10 * init + 1);
                cdl.countDown();
            }).start();
        }
        cdl.await();
        System.out.println(Arrays.stream(arr).sum());
    }

    private static void countByCyclicBarrier() {
        ConcurrentAddition concurrentAddition = new ConcurrentAddition();
        int threadCount = 10;
        int[] arr = new int[threadCount];

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount, () -> System.out.println(Arrays.stream(arr).sum()));
        for (int i = 0; i < threadCount; i++) {
            final int init = i;
            new Thread(() -> {
                arr[init] = concurrentAddition.addWithInitValue(10 * init + 1);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
