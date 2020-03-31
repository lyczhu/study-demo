package com.lawyus.study.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;


    public void produce() {
        kafkaTemplate.send("", "");
    }
}
