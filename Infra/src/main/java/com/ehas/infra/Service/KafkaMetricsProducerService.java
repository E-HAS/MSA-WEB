package com.ehas.infra.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class KafkaMetricsProducerService {

	@Value("${value.kafka.metrics.topic.services-metrics-name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMetricsProducerService(KafkaTemplate<String,String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendMessage(String message) {
        this.kafkaTemplate.send(topicName, message);
    }
}