package com.lawyus.study.concurrent.addition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentAdditionWithExecutor {

    public int addWithInitValue(int initValue) {
        int partTotal = 0;
        for (int i = 0; i < 10; i++) {
            partTotal += initValue + i;
        }
        return partTotal;
    }

    public void countByExecutorService() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            List<Future<Integer>> futures = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                final int index = i;
                Future<Integer> future = executor.submit(() -> addWithInitValue(10 * index + 1));
                futures.add(future);
            }

            int total = 0;
            for (Future<Integer> future : futures) {
                total += future.get();
            }

            System.out.println("ExecutorService结果: " + total);
            executor.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ConcurrentAdditionWithExecutor().countByExecutorService();
    }
}

