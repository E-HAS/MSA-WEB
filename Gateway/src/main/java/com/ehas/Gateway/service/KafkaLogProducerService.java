package com.ehas.Gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaLogProducerService {

	@Value("${value.kafka.metrics.topic.services-metrics-name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaLogProducerService(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        System.out.println(String.format("Produce message : %s", message));
        this.kafkaTemplate.send(topicName, message);
    }
}