package com.ehas.infra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableEurekaServer
@EnableConfigServer
@EnableScheduling
@EnableKafka
@EnableAsync
public class InfraApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraApplication.class, args);
	}

	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
		EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		config.setSecurePortEnabled(true);
		return config;
	}
}
