package com.lawyus.study.forkjoinpool;

import org.springframework.util.StopWatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class Fibonacci2 extends RecursiveTask<Integer> {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Fibonacci2 fibonacci2 = new Fibonacci2(30);
        StopWatch sw = new StopWatch("countTime");
        sw.start("Fibonacci2");
        Integer result = forkJoinPool.invoke(fibonacci2);
        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println(result);
    }

    int n;
    public Fibonacci2(int n) {
        this.n = n;
    }


    static ConcurrentHashMap<Integer, Integer> records = new ConcurrentHashMap<>();
    @Override
    protected Integer compute() {
        if (n <= 1) return n;
        if (records.get(n) != null) {
            return records.get(n);
        }

        Fibonacci2 f1 = new Fibonacci2(n - 1);
        f1.fork();

        Fibonacci2 f2 = new Fibonacci2(n - 2);
        int value = f2.compute() + f1.join();
        records.put(n, value);
        return value;
    }
}
