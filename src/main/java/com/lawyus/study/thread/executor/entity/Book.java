package com.lawyus.study.thread.executor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * @author lyc
 * @since 2021/4/29
 */
@Data
@Builder
@TableName("t_book")
public class Book {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}
