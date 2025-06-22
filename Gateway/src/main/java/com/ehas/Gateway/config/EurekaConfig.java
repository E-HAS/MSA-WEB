package com.ehas.Gateway.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class EurekaConfig implements ApplicationListener<WebServerInitializedEvent>{
	private final EurekaInstanceConfigBean instance;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String ip = instance.getIpAddress();

        instance.setNonSecurePort(port);
        instance.setSecurePort(port);
        instance.setSecurePortEnabled(true);

        String baseUrl = "https://" + ip + ":" + port;
        
        instance.setStatusPageUrl(baseUrl + "/actuator/info");
        instance.setHealthCheckUrl(baseUrl + "/actuator/health");
        instance.setHomePageUrl(baseUrl);
    }
}