package com.lawyus.study.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class MessageProducer {

    private static final String TOPIC = "topic_01";
    static Producer<String, String> producer = ProducerCreator.createProducer();
    static ProducerRecord<String, String> record =
            new ProducerRecord<>(TOPIC, "hello, Kafka!");

    public static void main(String[] args) {

        try {
            //send message
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Record sent to partition " + metadata.partition()
                    + " with offset " + metadata.offset());
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
        producer.close();
    }
}
