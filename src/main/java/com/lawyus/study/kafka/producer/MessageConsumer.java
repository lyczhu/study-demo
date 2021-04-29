package com.lawyus.study.kafka.producer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Collections;

/**
 * @author lyc
 * @since 2020/8/31
 */
public class MessageConsumer {
    private static final String TOPIC = "topic_01";

    static Consumer<String, String> consumer = ConsumerCreator.createConsumer();
// 循环消费消息

    public static void main(String[] args) {
        while (true) {
            //subscribe topic and consume message
            consumer.subscribe(Collections.singletonList(TOPIC));

            ConsumerRecords<String, String> consumerRecords =
                    consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("Consumer consume message:" + consumerRecord.value());
            }
        }
    }
}
