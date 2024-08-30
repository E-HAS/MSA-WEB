package com.ehas.consumerServer.API;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class kafkaConsumerApi {
    @KafkaListener(
    		topics = "${message.kafka.consumer.topic.name}"
    		, groupId = "${message.kafka.consumer.group-id}"
    		, containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
}