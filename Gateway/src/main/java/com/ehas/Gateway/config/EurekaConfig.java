package com.ehas.Gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaConfig {

    private final EurekaInstanceConfigBean eurekaInstanceConfig;

    @Autowired
    public EurekaConfig(EurekaInstanceConfigBean eurekaInstanceConfig) {
        this.eurekaInstanceConfig = eurekaInstanceConfig;
    }

    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        eurekaInstanceConfig.setSecurePort(port);
        eurekaInstanceConfig.setSecurePortEnabled(true);
        eurekaInstanceConfig.setPreferIpAddress(true);
        eurekaInstanceConfig.setNonSecurePortEnabled(false);
    }
}