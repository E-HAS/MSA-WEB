package com.XEKIDD.WebRTC.Handler;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.XEKIDD.WebRTC.Domain.Room;
import com.XEKIDD.WebRTC.Domain.WebSocketMessage;
import com.XEKIDD.WebRTC.Domain.WebSocketMessage.WebSocketMessageBuilder;
import com.XEKIDD.WebRTC.Repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {
		private final RoomRepository roomRepository;
	    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	    private final ObjectMapper objectMapper = new ObjectMapper();
	 
	    // message types, used in signalling:
	    // text message
	    private static final String MSG_TYPE_TEXT = "text";
	    // SDP Offer message
	    private static final String MSG_TYPE_OFFER = "offer";
	    // SDP Answer message
	    private static final String MSG_TYPE_ANSWER = "answer";
	    // New ICE Candidate message
	    private static final String MSG_TYPE_ICE = "ice";
	    // join room data message
	    private static final String MSG_TYPE_JOIN = "join";
	    // leave room data message
	    private static final String MSG_TYPE_LEAVE = "leave";

	    @PostConstruct
	    public void init() {
	    	roomRepository.addRoom(new Room("1"));
	    }
	    
	    @Override
	    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
	        logger.info("Session closed with status {}", status);
	        roomRepository.removeRoomMaps(session.getId());
	    }

	    @Override
	    public void afterConnectionEstablished(final WebSocketSession session) {
	    	 logger.info("Session Connect with SessionId {}", session);
	         sendMessage(session, WebSocketMessage
	        		 				.builder()
	        		 				.from("Server")
	        		 				.type(MSG_TYPE_JOIN)
	        		 				.build());
	         
	    }

	    @Override
	    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
	        // a message has been received
	    	logger.info("handleTextMessage {}",session);
	    	logger.info("handleTextMessage PayLoad{}",textMessage.getPayload());
	    	
	        try {
	        	 WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	        	 String userName = message.getFrom();
	        	 
	        	 Room room = null;
	        	 room = roomRepository.getRoomInRoomMaps(session.getId());
	        	 
	        	 switch(message.getType()) {
	        	 case  MSG_TYPE_JOIN:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_JOIN {}",session);
	        		 
	        		 String joinRoomId = message.getData();
	        		 room = roomRepository.findRoomByStringId(joinRoomId) // 해당 방번호가 있는지 확인
	                            .orElseThrow(() -> new IOException("Invalid room number received!"));
	        		 
	        		 roomRepository.addClient(room, userName, session); // 해당 방에 이름, 세션 저장 
	        		 roomRepository.putRoomMaps(session.getId(), room); //해당 세션이 들어가있는 방 정보 추가
	        		 
	        		 break;
	        	 case MSG_TYPE_OFFER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_OFFER {}",session);
	        		 
	        		 if(room != null) {
	        			 sendSDPinOtherSessions(room, message);
	                 }
	        		 
	        		 break;
	        	 case MSG_TYPE_ANSWER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ANSWER {}",session);
	        		 
	        		 if(room != null) {
	        			 sendSDPinOtherSessions(room, message);
	                 }
	        		 
	        		 break;
	        	 case  MSG_TYPE_ICE:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ICE {}",session);
	                 
	                 if(room != null) {
	        			 sendSDPinOtherSessions(room, message);
	                 }
	                 
	        		 break;
	        	 default:
	        		 logger.info("handleTextMessage >>>> {} {}",message.getType(), session);
	        		 break;
	        	 
	        	 }
	            //WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	            //logger.debug("[ws] Message of {} type from {} PayLoad received", message.getType(), message.getPayload());

	        } catch (IOException e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }
	    }

	    private void sendMessage(WebSocketSession session, WebSocketMessage webSocketMessage) {
	        try {
	            String json = objectMapper.writeValueAsString(webSocketMessage);
	            session.sendMessage(new TextMessage(json));
	        } catch (IOException e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }
	    }
	    
	    private void sendSDPinOtherSessions(Room room, WebSocketMessage message) {
       	 Map<String, WebSocketSession> clients = roomRepository.getClients(room); // 해당 룸에 접속한 세션들 가져오기
       	 	for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  { 
       	 		if (!client.getKey().equals(message.getFrom())) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
	
                 sendMessage(client.getValue(), WebSocketMessage
                		 						.builder()
                		 						.from(message.getFrom())
                		 						.type(message.getType())
                		 						.data(message.getData())
                		 						.candidate(message.getCandidate())
                		 						.sdp(message.getSdp())
                		 						.build());
       	 		}
       	 	} 
	    }

	}