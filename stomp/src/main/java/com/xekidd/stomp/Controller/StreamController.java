package com.xekidd.stomp.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.xekidd.stomp.Entity.StompMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@MessageMapping("/Stream")
public class StreamController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	// @Payload @Header
	 
	private static List<String> lists = new CopyOnWriteArrayList<String>();
	
    @MessageMapping("/Send/{RoomId}/Join")
    public void sendJoinMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){
    	
    	lists.add(data.getFrom());
    	StompMessage msg = data;
    	msg.setData(lists);
    	String to = msg.getTo();
    	log.info("sendJoinMsg {} -> {}",data.getFrom(), to);
    	
        simpMessagingTemplate.convertAndSend("/topic/Stream/Receive/"+RoomId+"/Join/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Offer")
    public void sendOfferMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){  
    	StompMessage msg =  data;
    	String to = msg.getTo();
    	log.info("sendOfferMsg {} -> {}",data.getFrom(), to);
    	
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Offer/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Answer")
    public void sendAnswerMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){  
    	
    	StompMessage msg =  data;
    	String to = msg.getTo();
    	log.info("sendAnswerMsg {} -> {}",data.getFrom(), to);
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Answer/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Ice")
    public void sendIceMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){ 
    	
    	StompMessage msg =  data;
    	String to = msg.getTo();
    	log.info("sendIceMsg {} -> {}",data.getFrom(), to);
    	
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Ice/"+to,msg);
    }
    
    
    @MessageMapping("/Send/{RoomId}/Msg")
    public void sendMsgMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){  
    	
    	StompMessage msg =  data;
    	
        simpMessagingTemplate.convertAndSend("/topic/Stream/Receive/"+RoomId+"/Msg",msg);
    }
}
