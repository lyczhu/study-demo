package com.lawyus.study.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * TODO describe please!
 *
 * @author yushing
 * @date 2020/3/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MongoConnTest {

    @Autowired
    private MongoConn mongoConn;

    @Test
    public void test() {
        mongoConn.test("localhost", 27017, "local");
    }

}