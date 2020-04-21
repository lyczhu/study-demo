package com.lawyus.study.generic;

import com.lawyus.study.rtsp.Client;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO describe please!
 *
 * @author yushing
 * @since 2020/4/5
 */
public class GenericTree {

    public static Tree genTree() {

        List<Document> list = new ArrayList<>();
        Document document = new Document();
        document.append("name", "car");

        list.add(document);
        Tree<Document> tree = new Tree<>();
        tree.setSeed(list);

        return tree;
    }

    public static void mongo(Tree tree) {
        String host = "localhost";
        int port = 27017;
        String database = "city";
        String urlStr = "mongodb://" + host + ":" + port;

        ConnectionString cString = new ConnectionString(urlStr);
        MongoClientSettings mcs = MongoClientSettings.builder().applyConnectionString(cString)
                .applyToClusterSettings(c -> c.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();
        MongoClient mongoClient = MongoClients.create(mcs);

        MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();

        databaseNames.first();
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        MongoCollection<Document> collection = mongoDatabase.getCollection("car");
        collection.insertMany(tree.getSeed());
        System.out.println("数据写入成功");

    }

    public static void main(String[] args) {
        mongo(genTree());

    }

}
