package com.lawyus.study.concurrent.addition;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 并发加法
 * 用10个线程从1加到100，第1个线程从1加到10，第2个线程从11加到20，...，等10个线程都加完后再求总和
 *
 */
public class ConcurrentAddition {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentAddition addition = new ConcurrentAddition();
        addition.countByCountDownLatch();
        addition.countByCyclicBarrier();
    }

    public int addWithInitValue(int initValue) {
        int partTotal = 0;
        for (int i = 0; i < 10; i++) {
            partTotal = partTotal + initValue + i;
        }
        return partTotal;
    }

    private void countByCountDownLatch() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount);
        int[] arr = new int[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int init = i;
            new Thread(() -> {
                arr[init] = addWithInitValue(10 * init + 1);
                cdl.countDown();
            }).start();
        }
        cdl.await();
        System.out.println(Arrays.stream(arr).sum());
    }

    private void countByCyclicBarrier() {
        int threadCount = 10;
        int[] arr = new int[threadCount];

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount, () -> System.out.println(Arrays.stream(arr).sum()));
        for (int i = 0; i < threadCount; i++) {
            final int init = i;
            new Thread(() -> {
                arr[init] = addWithInitValue(10 * init + 1);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
