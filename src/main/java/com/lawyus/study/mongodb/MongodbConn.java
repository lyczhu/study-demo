package com.lawyus.study.mongodb;

import com.mongodb.MongoClient;

public class MongodbConn {

    public static void main(String[] args) {
        MongoClient client = new MongoClient("", 23);
        client.getDatabase("");
    }
}
