package com.ehas.auth;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaLogProducerConfig {
	@Value("${message.kafka.producer.bootstrap-servers}")
	private String bootstrapServer;
	
	@Value("${message.kafka.producer.key-serializer}")
	private String keySerializer;
	
	@Value("${message.kafka.producer.value-serializer}")
	private String valueSerializer;
	
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
}