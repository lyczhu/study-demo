package com.lawyus.study.thread.executor.service.impl;

import com.lawyus.study.thread.executor.entity.Book;
import com.lawyus.study.thread.executor.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author lyc
 * @since 2021/4/29
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class BookServiceImplTest {

    private static ExecutorService es = new ThreadPoolExecutor(6, 6,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    @Resource
    private BookService bookService;

    @Test
    public void iterate() {
        es.execute(this::thread1);
        es.execute(this::thread2);
        es.execute(this::thread3);
        es.execute(this::thread4);
        /*es.execute(this::thread5);
        es.execute(this::thread6);*/
    }

    private void thread1() {
        for (int i = 0; i < 100; i++) {
            log.info("线程1: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }

    private void thread2() {
        for (int i = 0; i < 100; i++) {
            log.info("线程2: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }

    private void thread3() {
        for (int i = 0; i < 100; i++) {
            log.info("线程3: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }

    private void thread4() {
        for (int i = 0; i < 100; i++) {
            log.info("线程4: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }

    private void thread5() {
        for (int i = 0; i < 100; i++) {
            log.info("线程5: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }

    private void thread6() {
        for (int i = 0; i < 100; i++) {
            log.info("线程6: {}", i);
            Book book = new Book();
            book.setName(RandomStringUtils.randomAscii(20));
            bookService.save(book);
        }
    }
}