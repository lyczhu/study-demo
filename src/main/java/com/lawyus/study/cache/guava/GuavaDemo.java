package com.lawyus.study.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.concurrent.ExecutionException;

public class GuavaDemo {

    public static void main(String[] args) throws ExecutionException {
        Cache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 10000;
                    }
                });
        cache.put("k", 10);

        System.out.println(cache.get("kk", () -> 10000));
        cache.asMap().forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
