package com.lawyus.study.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.springframework.stereotype.Service;

@Service
public class MongoConn {

    public void test(String host, int port, String database) {
        try {
            MongoClientOptions options = MongoClientOptions.builder().socketTimeout(0).connectTimeout(0).serverSelectionTimeout(5000).build();
            MongoClient client = new MongoClient(new ServerAddress(host, port), options);

            MongoIterable<String> databaseNames = client.listDatabaseNames();

            databaseNames.first();
            MongoDatabase mongoDatabase = client.getDatabase(database);
            MongoIterable<String> collectionNames = mongoDatabase.listCollectionNames();
            collectionNames.first();
        } catch (MongoSocketOpenException | MongoTimeoutException e) {
            System.out.println("MongoDB连接超时");
        }
    }

    public void testByTemplate(String host, int port, String database) {

    }
}
