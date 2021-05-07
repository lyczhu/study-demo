package com.lawyus.study.thread.executor.service.impl;

import com.lawyus.study.thread.executor.entity.Book;
import com.lawyus.study.thread.executor.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.common.metrics.stats.Count;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.concurrent.*;

import static org.junit.Assert.*;

/**
 * @author lyc
 * @since 2021/4/29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class BookServiceImplTest {

    private static final ExecutorService es = new ThreadPoolExecutor(6, 6,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10000), new ThreadPoolExecutor.CallerRunsPolicy());


    @Resource
    private BookService bookService;

    @Test
    public void iterate() {
        CountDownLatch cdl = new CountDownLatch(100000);
        for (int i = 0; i < 100000; i++) {
            int finalI = i;
            es.execute(() -> {
                log.info("正在执行第 {} 个线程", finalI);
                bookService.save(Book.builder().id((long) finalI).name(RandomStringUtils.randomAscii(20)).build());
                cdl.countDown();
            });
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}