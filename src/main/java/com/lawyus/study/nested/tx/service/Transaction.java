package com.lawyus.study.nested.tx.service;

import com.lawyus.study.nested.tx.dao.TableADao;
import com.lawyus.study.nested.tx.dao.TableBDao;
import com.lawyus.study.nested.tx.entity.TableA;
import com.lawyus.study.nested.tx.entity.TableB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/17 14:20
 */
@Service
@Slf4j
public class Transaction {

	@Autowired
	private TableADao tableADao;

	@Autowired
	private TableBDao tableBDao;

	@Autowired
	private TransactionTwo transactionTwo;

	@Transactional
	public void methodA() {
		TableA a = new TableA();
		a.setName("tableA");
		tableADao.insert(a);
		try {
			transactionTwo.methodB();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void methodB() {
		TableB b = new TableB();
		b.setName("tableB");
		tableBDao.insert(b);
		System.out.println(1/0);
	}
}
