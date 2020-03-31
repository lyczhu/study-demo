package com.lawyus.study.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MongoConn {

    public void test(String host, int port, String database) {
        String urlStr = "mongodb://" + host + ":" + port;
        try {
            ConnectionString cString = new ConnectionString(urlStr);
            MongoClientSettings mcs = MongoClientSettings.builder().applyConnectionString(cString)
                    .applyToClusterSettings(c -> c.serverSelectionTimeout(5, TimeUnit.SECONDS))
                    .build();
            MongoClient mongoClient = MongoClients.create(mcs);

            MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();

            databaseNames.first();
            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
            MongoIterable<String> collectionNames = mongoDatabase.listCollectionNames();
            collectionNames.first();
        } catch (MongoTimeoutException e) {
            System.out.println("MongoDB连接超时");
        }
    }

    public void testByTemplate(String host, int port, String database) {

    }
}
