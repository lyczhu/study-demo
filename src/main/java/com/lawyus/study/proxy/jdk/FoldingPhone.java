package com.lawyus.study.proxy.jdk;

/**
 * TODO : Describe please!
 *
 * @author: lyc
 * @date: 2019/12/20 16:34
 * @since: 1.0.0
 */
public class FoldingPhone implements Phone {
	@Override
	public void call() {
		System.out.println("make a telephone call by folding phone");
	}
}
