package com.ehas.auth.service;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "${message.topic.name}", groupId = ConsumerConfig.GROUP_ID_CONFIG)
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
}