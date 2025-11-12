package com.lawyus.study.concurrent.forkjoinpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class WordCounter {

    public static void main(String[] args) {
        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};

        Map<String, Integer> result;
        try (ForkJoinPool fjp = new ForkJoinPool()) {
            Counter counter = new Counter(fc, 0, fc.length - 1);
            result = fjp.invoke(counter);
        }
        result.forEach((e, v) -> System.out.println(e + " " + v));
    }

    static class Counter extends RecursiveTask<Map<String, Integer>> {

        String[] lines;
        int s, e;

        public Counter(String[] lines, int start, int end) {
            this.lines = lines;
            this.s = start;
            this.e = end;
        }

        @Override
        protected Map<String, Integer> compute() {
            if (s >= e) {
                return calculate(lines[s]);
            }
            int m = (s + e) / 2;
            Counter r1 = new Counter(lines, s, m);
            r1.fork();
            Counter r2 = new Counter(lines, m + 1, e);

            return merge(r2.compute(), r1.join());
        }

        private Map<String, Integer> merge(Map<String, Integer> result1, Map<String, Integer> result2) {
            Map<String, Integer> result = new HashMap<>(result1);

            result2.forEach((k, v) -> {
                result.merge(k, v, Integer::sum);
            });
            return result;
        }

        private Map<String, Integer> calculate(String line) {
            String[] words = line.split("\\s+");

            Map<String, Integer> result = new HashMap<>();
            for (String word : words) {
                result.merge(word, 1, Integer::sum);
            }
            return result;
        }
    }

}
