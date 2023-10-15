package com.xekidd.stomp.Controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@MessageMapping("/Stream")
public class StreamController {
	private final SimpMessagingTemplate simpMessagingTemplate;

	 
    @MessageMapping("/send")
    //@SendTo("/topic/greeting")
    public void sendMsg(@Payload Map<String,Object> data){
        simpMessagingTemplate.convertAndSend("/topic/1",data);
    }
}
