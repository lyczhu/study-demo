package com.lawyus.study.thread.executor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lawyus.study.thread.executor.dao.BookMapper;
import com.lawyus.study.thread.executor.entity.Book;
import com.lawyus.study.thread.executor.service.BookService;
import org.springframework.stereotype.Service;

/**
 * @author lyc
 * @since 2021/4/29
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}
