package com.lawyus.study.nested.tx.service;

import com.lawyus.study.nested.tx.dao.TableBDao;
import com.lawyus.study.nested.tx.entity.TableB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/17 18:11
 */
@Service
public class TransactionTwo {

	@Autowired
	private TableBDao tableBDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void methodB() {
		TableB b = new TableB();
		b.setName("tableB");
		tableBDao.insert(b);
		System.out.println(1/0);
	}
}
