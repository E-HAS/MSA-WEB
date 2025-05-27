package com.ehas.centerGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;
import reactor.netty.http.Http2SslContextSpec;


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
    
    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setSecurePortEnabled(true); // 중요
        return config;
    }
    
    @Bean
    public HttpClient httpClient() {
        return HttpClient.create().secure(spec -> {
            spec.sslContext(Http2SslContextSpec.forClient()
                    .configure(sslContextBuilder -> 
                        sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE)
                    ));
        });
    }
}
