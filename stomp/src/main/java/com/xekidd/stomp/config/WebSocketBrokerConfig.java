package com.xekidd.stomp.config;



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
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/Stomp")
        		.setAllowedOriginPatterns("*")
        		.withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
    	registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
            	
            	
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                StompCommand stompStatus = accessor.getCommand();
                String sessenId = accessor.getSessionId();
                
                MessageHeaders headers = message.getHeaders();
                
                //MultiValueMap<String, String> maps= headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
                // 1:N Map
                
                switch (accessor.getCommand()) {
                case CONNECT:
                	String type = accessor.getFirstNativeHeader("Type");
                	String userId = accessor.getFirstNativeHeader("UserId");
                	log.info("CONNECT SessionId: {}, Type : {}, UserId : {}",sessenId, type, userId);
                    break;
                case DISCONNECT:
                	log.info("DISCONNECT SessionId: "+ sessenId);
                    break;
                default:
                    break;
                }
                
                return message;
            }
        });
    }

}
