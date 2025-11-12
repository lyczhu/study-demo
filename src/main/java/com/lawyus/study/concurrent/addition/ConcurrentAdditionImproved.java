package com.lawyus.study.concurrent.addition;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentAdditionImproved {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentAdditionImproved addition = new ConcurrentAdditionImproved();

        // 使用CountDownLatch的方式
        addition.countByCountDownLatch();
        // 使用CyclicBarrier的方式
        addition.countByCyclicBarrier();
    }

    /**
     * 计算从initValue开始的连续10个数的和
     * @param initValue 起始值
     * @return 连续10个数的和
     */
    public int addWithInitValue(int initValue) {
        int partTotal = 0;
        for (int i = 0; i < 10; i++) {
            partTotal += initValue + i;
        }
        return partTotal;
    }

    private void countByCountDownLatch() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch cdl = new CountDownLatch(threadCount);
        int[] arr = new int[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            new Thread(() -> {
                arr[index] = addWithInitValue(10 * index + 1);
                cdl.countDown();
            }, "Thread-" + index).start();
        }

        cdl.await();
        int result = Arrays.stream(arr).sum();
        System.out.println("CountDownLatch结果: " + result);
    }

    private void countByCyclicBarrier() {
        int threadCount = 10;
        int[] arr = new int[threadCount];

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount,
            () -> System.out.println("CyclicBarrier结果: " + Arrays.stream(arr).sum()));

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            new Thread(() -> {
                arr[index] = addWithInitValue(10 * index + 1);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }, "Thread-" + index).start();
        }
    }
}
