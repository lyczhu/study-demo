package com.lawyus.study.nested.tx.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionTest {

	@Autowired
	private Transaction transaction;

	@Test
	public void methodA() {
		transaction.methodA();
	}

	@org.junit.Test
	public void methodB() {
	}
}