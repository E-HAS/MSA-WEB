package com.ehas.centerGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
@EnableDiscoveryClient
public class CenterGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CenterGatewayApplication.class, args);
	}
	
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity security) {
    	return security
    		    .csrf(csrf -> csrf.disable())
    		    .build();
    }
}
