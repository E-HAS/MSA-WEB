package com.ehas.infra.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaMetricsProducerConfig {
	@Value("${value.kafka.metrics.bootstrap-servers}")
	private String bootstrapServer;
	
	@Value("${value.kafka.metrics.key-serializer}")
	private String keySerializer;
	
	@Value("${value.kafka.metrics.value-serializer}")
	private String valueSerializer;
	
	@Value("${value.kafka.metrics.topic.services-metrics-name}")
    private String topicName;
	
    @Bean
    public ProducerFactory<String,String> producerFactory(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new DefaultKafkaProducerFactory<>(properties);
    }
    @Bean
    public KafkaTemplate<String,String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
    
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return new KafkaAdmin(configs);
    }
    
    @Bean
    public NewTopic metricsTopic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .config("retention.ms", "3600000") // 1시간 = 1*60*60*1000ms
                .build();
    }
}