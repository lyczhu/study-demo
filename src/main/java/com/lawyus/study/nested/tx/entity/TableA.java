package com.lawyus.study.nested.tx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * TODO : Describe please!
 *
 * @author: lyc
 * @date: 2019/12/17 15:43
 * @since: 1.0.0
 */
@Data
public class TableA {

	@TableId(type = IdType.AUTO)
	private Integer id;

	private String name;
}
