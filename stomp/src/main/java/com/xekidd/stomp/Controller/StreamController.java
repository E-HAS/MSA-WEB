package com.xekidd.stomp.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.xekidd.stomp.Entity.StomeRoomMessageEntity;
import com.xekidd.stomp.Entity.StompMessage;
import com.xekidd.stomp.Redis.Entity.MeetRoom;
import com.xekidd.stomp.Redis.Entity.MeetRoomUser;
import com.xekidd.stomp.Repository.RoomMessageJpaRepository;
import com.xekidd.stomp.Service.MeetRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Slf4j
@MessageMapping("/Stream")
public class StreamController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final MeetRoomService meetRoomService;
	
	private final RoomMessageJpaRepository roomMessageJpaRepository;
	// @Payload @Header
	
    @MessageMapping("/Send/{RoomId}/Join")
    public void sendJoinMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){
    	String to = data.getTo();
    	String from = data.getFrom();
    	String roomId = data.getRoomId();
    	
    	List<String> UserNameListInRoom = meetRoomService.findUserNameListInRoomByRoomId(RoomId);
    	
    	StompMessage msg = new StompMessage();
    	msg.setTo(to);
    	msg.setFrom(from);
    	msg.setRoomId(roomId);
    	msg.setData(UserNameListInRoom);
    	
    	log.info("roomId : {} sendJoinMsg {} -> {}", roomId,  from, to);
    	
        simpMessagingTemplate.convertAndSend("/topic/Stream/Receive/"+RoomId+"/Join/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Offer")
    public void sendOfferMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){  
    	StompMessage msg =  data;
    	String to = data.getTo();
    	String from = data.getFrom();
    	
    	log.info("roomId : {} sendOfferMsg {} -> {}",RoomId,from, to);
    	
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Offer/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Answer")
    public void sendAnswerMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){  
    	
    	StompMessage msg =  data;
    	String to = data.getTo();
    	String from = data.getFrom();
    	
    	log.info("roomId : {} sendAnswerMsg {} -> {}",RoomId ,from, to);
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Answer/"+to,msg);
    }
    
    @MessageMapping("/Send/{RoomId}/Ice")
    public void sendIceMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId){ 
    	
    	StompMessage msg =  data;
    	String to = data.getTo();
    	String from = data.getFrom();
    	
    	log.info("roomId : {} sendIceMsg {} -> {}",RoomId ,from, to);
    	
        simpMessagingTemplate.convertAndSend("/queue/Stream/Receive/"+RoomId+"/Ice/"+to,msg);
    }
    
    
    @Transactional
    @MessageMapping("/Send/{RoomId}/Msg")
    public void sendMsgMsg( @RequestBody StompMessage data
    					,@DestinationVariable String RoomId
    					){  
    	
    	LocalDateTime dt = LocalDateTime.now();
    	//String userAddress = request.getRemoteAddr();
    	
    	StomeRoomMessageEntity entity = StomeRoomMessageEntity.builder()
    														.reqDt(dt)
    														.roomId(RoomId)
    														.userName(data.getFrom())
    														//.userAddress(userAddress)
    														.roomMessage(data.getRoomMessage())
    														.build();
    	roomMessageJpaRepository.save(entity);
    	
    	StompMessage msg =  data;
        simpMessagingTemplate.convertAndSend("/topic/Stream/Receive/"+RoomId+"/Msg",msg);
    }
}
