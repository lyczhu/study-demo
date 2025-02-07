package com.lawyus.study.nested.tx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lawyus.study.nested.tx.dao.TableADao;
import com.lawyus.study.nested.tx.entity.TableA;
import com.lawyus.study.nested.tx.service.TxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TxServiceImpl implements TxService {

    @Autowired
    private TableADao tableADao;

    @Override
    @Transactional
    public List<TableA> select() {
        QueryWrapper<TableA> queryWrapper = new QueryWrapper<>();
        return tableADao.selectList(queryWrapper);
    }
}
