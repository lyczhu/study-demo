package com.lawyus.study.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * TODO describe please!
 *
 * @author yushing
 * @since 2020/3/28
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MongoConnTest {

    @Autowired
    private MongoConn mongoConn;

    @Test
    public void testConn() {
        mongoConn.testConn("localhost", 27017, "local");
    }

    @Test
    public void save() {
        mongoConn.save("localhost", 27017, "city", "car");
    }
}