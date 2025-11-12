package com.lawyus.study.concurrent.addition;

import java.util.Arrays;
import java.util.concurrent.Phaser;

public class ConcurrentAdditionWithPhaser {

    public int addWithInitValue(int initValue) {
        int partTotal = 0;
        for (int i = 0; i < 10; i++) {
            partTotal = partTotal + initValue + i;
        }
        return partTotal;
    }

    private void countByPhaser() {
        int threadCount = 10;
        int[] arr = new int[threadCount];

        // 创建Phaser，注册主线程
        Phaser phaser = new Phaser(1);

        for (int i = 0; i < threadCount; i++) {
            // 每创建一个线程前先注册到phaser
            phaser.register();
            final int index = i;
            new Thread(() -> {
                try {
                    arr[index] = addWithInitValue(10 * index + 1);
                } finally {
                    // 线程完成工作后到达屏障
                    phaser.arriveAndDeregister();
                }
            }).start();
        }

        // 主线程到达屏障并等待所有子线程完成
        phaser.arriveAndAwaitAdvance();
        System.out.println("Phaser结果: " + Arrays.stream(arr).sum());
    }

    public static void main(String[] args) {
        new ConcurrentAdditionWithPhaser().countByPhaser();
    }
}

