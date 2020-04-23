package com.lawyus.study.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lawyus.study.util.ObjectIdSerializer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.MongoDatabaseUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * TODO describe please!
 *
 * @author yushing
 * @since 2020/3/28
 *
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j*/
public class MongoConnTest {

    @Autowired
    private MongoConn mongoConn;

    @Test
    public void testConn() {
        mongoConn.testConn("localhost", 27017, "local");
    }

    @Test
    public void save() {
        MongoClient client = MongoUtils.client("localhost", 27017);
        MongoDatabase db = client.getDatabase("city");
        MongoCollection<Document> col = db.getCollection("car");

        long start = System.currentTimeMillis();
        for (int j = 0; j < 100; j++) {
            List<Document> list = new ArrayList<>();
            for (int i = 0; i < 100000; i++) {
                System.out.println("生成数据" + i);
                Random r = new Random();
                byte[] bytes = new byte[6];
                r.nextBytes(bytes);
                Document d = new Document();
                d.append("name", Arrays.toString(bytes));
                list.add(d);
            }
            col.insertMany(list);
        }
        System.out.println("消耗时间：" + (System.currentTimeMillis() - start));

    }

    @Test
    public void json()  throws IOException {
        MongoClient client = MongoUtils.client("localhost", 27017);
        MongoDatabase db = client.getDatabase("city");
        MongoCollection<Document> col = db.getCollection("car");

        MongoClient client2 = MongoUtils.client("localhost", 27018);
        MongoDatabase db2 = client2.getDatabase("city");
        MongoCollection<Document> col2 = db2.getCollection("car");

        long count = col.countDocuments();
        int limit = 1000;

        for (int start = 0; start < count; start += limit) {
            System.out.println("写入数据：" + start);
            List<Document> list = col.find().skip(start).limit(limit).into(new ArrayList<>());
            List<Document> docs = getDocuments(list);
            docs.forEach(e -> {
                e.append("_id", new ObjectId(String.valueOf(e.get("_id"))));
            });
            col2.insertMany(docs);
        }
        client.close();
        client2.close();
    }

    private List<Document> getDocuments(List<Document> list) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        mapper.registerModule(module);

        ObjectReader reader = mapper.readerFor(new TypeReference<List<Document>>() {});
        return reader.readValue(mapper.writeValueAsBytes(list));
    }
}