package com.lawyus.study.forkjoinpool;

import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordCounter {

    public static void main(String[] args) {
        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};

        ForkJoinPool fjp = new ForkJoinPool();
        Counter counter = new Counter(fc, 0, fc.length - 1);
        HashMap<String, Integer> result = fjp.invoke(counter);
        result.forEach((e, v) -> System.out.println(e + " " + v));
    }

    static class Counter extends RecursiveTask<HashMap<String, Integer>> {

        String[] lines;
        int s, e;

        public Counter(String[] lines, int start, int end) {
            this.lines = lines;
            this.s = start;
            this.e = end;
        }

        @Override
        protected HashMap<String, Integer> compute() {
            if (s >= e) {
                return calculate(lines[s]);
            }
            int m = (s + e) / 2;
            Counter r1 = new Counter(lines, s, m);
            r1.fork();
            Counter r2 = new Counter(lines, m + 1, e);

            return merge(r2.compute(), r1.join());
        }

        private HashMap<String, Integer> merge(HashMap<String, Integer> result1, HashMap<String, Integer> result2) {
            HashMap<String, Integer> result = new HashMap<>(result1);

            result2.forEach((e, v) -> {
                result.merge(e, v, Integer::sum);
            });
            return result;
        }

        private HashMap<String, Integer> calculate(String line) {

            String[] words = line.split("\\s+");

            HashMap<String, Integer> result = new HashMap<>();
            for (String word : words) {
                result.merge(word, 1, Integer::sum);
            }
            return result;
        }
    }

}
