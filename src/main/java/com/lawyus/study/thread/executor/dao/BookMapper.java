package com.lawyus.study.thread.executor.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lawyus.study.thread.executor.entity.Book;
import org.springframework.stereotype.Repository;

/**
 * @author lyc
 * @since 2021/4/29
 */
@Repository
public interface BookMapper extends BaseMapper<Book> {
}
