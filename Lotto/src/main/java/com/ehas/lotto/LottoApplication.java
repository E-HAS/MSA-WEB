package com.ehas.lotto;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
@EnableJpaAuditing
@EnableWebSecurity
@EnableMethodSecurity
@EnableKafka
public class LottoApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoApplication.class, args);
	}
	
	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
		EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		config.setSecurePortEnabled(true);
		config.setPreferIpAddress(true);
        config.setSecurePortEnabled(true);
		return config;
	}
	
	/*
	@Bean
	public ServletWebServerFactory servletContainer() {
	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
	    tomcat.addAdditionalTomcatConnectors(createHttpConnector());
	    return tomcat;
	}

	private Connector createHttpConnector() {
	    Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
	    connector.setScheme("http");
	    connector.setPort(8080); // HTTP 포트
	    connector.setSecure(false);
	    connector.setRedirectPort(8443); // HTTPS로 리디렉션
	    return connector;
	}
	*/
}
