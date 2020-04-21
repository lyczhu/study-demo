package com.lawyus.study.mongodb;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.connection.ConnectionPoolSettings;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MongoConn {

    public void testConn(String host, int port, String database) {
        String urlStr = "mongodb://" + host + ":" + port;
        ConnectionString cString = new ConnectionString(urlStr);
        MongoClientSettings mcs = MongoClientSettings.builder().applyConnectionString(cString)
                .applyToClusterSettings(c -> c.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();

        try (MongoClient mongoClient = MongoClients.create(mcs)) {
            // 测试连接不应使用 listDatabaseNames()方法或者listCollectionNames()方法，因为当使用了账号密码进行测试时，账号未必有执行这两个命令的权限
            /*MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
            databaseNames.first();
            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
            MongoIterable<String> collectionNames = mongoDatabase.listCollectionNames();
            collectionNames.first();*/

            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);

            Random random = new Random();
            byte[] bytes = new byte[6];
            random.nextBytes(bytes);
            MongoCollection<Document> col = mongoDatabase.getCollection(Arrays.toString(bytes));
            Document document = new Document().append("name", "testConn");
            col.insertOne(document);
            col.drop();
        } catch (MongoTimeoutException e) {
            log.info("MongoDB连接超时", e);
        }
    }

    public void save(String host, int port, String database, String collection) {
        String urlStr = "mongodb://" + host + ":" + port;
        ConnectionString cString = new ConnectionString(urlStr);
        MongoClientSettings mcs = MongoClientSettings.builder().applyConnectionString(cString)
                .applyToClusterSettings(c -> c.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .applyToConnectionPoolSettings(p -> {})
                .build();
        try (MongoClient mongoClient = MongoClients.create(mcs)) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
            MongoCollection<Document> col = mongoDatabase.getCollection(collection);

            Document document = new Document().append("_id", new ObjectId("5e89efff210ec806e2844826")).append("name", "car1");
            col.insertOne(document);
        } catch (MongoTimeoutException e) {
            log.info("MongoDB连接超时", e);
        }
    }

    public void testByTemplate(String host, int port, String database) {

    }
}
