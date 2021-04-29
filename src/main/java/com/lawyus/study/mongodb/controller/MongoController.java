package com.lawyus.study.mongodb.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutionException;

/**
 * TODO describe please!
 *
 * @author yushing
 * @since 2020/3/28
 */
@RestController
@RequestMapping("/mongo")
public class MongoController {

    public void test() {

    }


    @Autowired
    private LoadingCache<String, Integer> cache;

    @Bean
    public LoadingCache<String, Integer> chche() {
        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(12)
                .build(new CacheLoader<>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public Integer load(String s) throws Exception {
                        System.out.printf("生成缓存，缓存key: %s", s);
                        System.out.println();
                        return 10000;
                    }
                });
        return cache;
    }

    @GetMapping("/data")
    public int[] getData() throws ExecutionException {
        int[] num = new int[12];
        for (int i = 0; i < num.length; i++) {
            cache.get("key#" + i);
        }
        System.out.printf("cache: ");
        return num;
    }
}
