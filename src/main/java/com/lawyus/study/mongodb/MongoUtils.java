package com.lawyus.study.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.concurrent.TimeUnit;

public class MongoUtils {

    public static MongoClient client(String host, int port) {
        String urlStr = "mongodb://" + host + ":" + port;
        ConnectionString cString = new ConnectionString(urlStr);
        MongoClientSettings mcs = MongoClientSettings.builder().applyConnectionString(cString)
                .applyToClusterSettings(c -> c.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();

        return MongoClients.create(mcs);
    }
}
