package com.ehas.Log.handler;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class kafkaLogHandler {
    @KafkaListener(
    		topics = "${value.kafka.consumer.topic.name}"
    		, groupId = "${value.kafka.consumer.group-id}"
    		, containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
}