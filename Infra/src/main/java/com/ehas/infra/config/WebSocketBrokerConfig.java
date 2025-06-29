package com.ehas.infra.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
    	registry.setApplicationDestinationPrefixes("/ws"); // 클라이언트 -> 서버
        registry.enableSimpleBroker("/topic","/queue");     // 서버 -> 클라이언트
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/stomp")
        		.setAllowedOriginPatterns("*")
        		.withSockJS();
        registry.addEndpoint("/stomp")
				.setAllowedOriginPatterns("*");
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
    	registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
            	log.info(message.toString());
            	
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                StompCommand stompStatus = accessor.getCommand();
                String sessenId = accessor.getSessionId();
                String sessenToken = accessor.getFirstNativeHeader("Authorization");
                
                MessageHeaders headers = message.getHeaders();
                
                switch (accessor.getCommand()) {
                case CONNECT:
                    break;
                case DISCONNECT:
                    break;
                default:
                    break;
                }
                
                return message;
            }
        });
    }

}
