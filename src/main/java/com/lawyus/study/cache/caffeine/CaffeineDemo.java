package com.lawyus.study.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class CaffeineDemo {
    public static void main(String[] args) {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .build();
        cache.put("k", "v");
        System.out.println(cache.getIfPresent("k"));
    }
}
