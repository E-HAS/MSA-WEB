package com.XEKIDD.WebRTC.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.XEKIDD.WebRTC.Handler.SignalHandler;
import com.XEKIDD.WebRTC.Repository.RoomRepository;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
		private final SignalHandler signalHandler;
	
	   @Override
	    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	        registry.addHandler(signalHandler, "/signal")
	                .setAllowedOrigins("*"); // allow all origins
	    }


	    @Bean
	    public ServletServerContainerFactoryBean createWebSocketContainer() {
	        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
	        container.setMaxTextMessageBufferSize(8192);
	        container.setMaxBinaryMessageBufferSize(8192);
	        return container;
	    }
	}

