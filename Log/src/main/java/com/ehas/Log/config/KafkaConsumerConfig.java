package com.ehas.Log.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Service;


@Configuration
public class KafkaConsumerConfig {

	@Value("${value.kafka.metrics.bootstrap-servers}")
	private String bootstrapServer;
	
	@Value("${value.kafka.metrics.group-id}")
	private String groupId;
	
	@Value("${value.kafka.metrics.auto-offset-reset}")
	private String offsetReset;
	
	@Value("${value.kafka.metrics.key-deserializer}")
	private String keyDeserializer;
	
	@Value("${value.kafka.metrics.value-deserializer}")
	private String valueDeserializer;
	
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
	    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
	    return props;
	}
	
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }
    
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
