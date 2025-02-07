package com.lawyus.study.nested.tx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/17 15:43
 */
@Data
@TableName("tableA")
public class TableA {

	@TableId(type = IdType.AUTO)
	private Integer id;

	private String name;
}
