package com.ehas.centerGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaPortConfigurator {

    private final EurekaInstanceConfigBean eurekaInstanceConfig;

    @Autowired
    public EurekaPortConfigurator(EurekaInstanceConfigBean eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }

    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        eurekaInstanceConfig.setSecurePortEnabled(true); // 이 줄도 꼭 필요!
        eurekaInstanceConfig.setSecurePort(port);
        System.out.println("Eureka secure port set to: " + port);
    }
}