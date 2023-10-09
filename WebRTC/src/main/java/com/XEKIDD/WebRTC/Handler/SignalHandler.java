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

import com.XEKIDD.WebRTC.Domain.MeetRoom;
import com.XEKIDD.WebRTC.Domain.MeetUserSession;
import com.XEKIDD.WebRTC.Domain.WebSocketMessage;
import com.XEKIDD.WebRTC.Domain.WebSocketMessage.WebSocketMessageBuilder;
import com.XEKIDD.WebRTC.Redis.Entity.UserSession;
import com.XEKIDD.WebRTC.Redis.Repository.UserSessionRepository;
import com.XEKIDD.WebRTC.Repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler {
		private final RoomRepository roomRepository;
		private final UserSessionRepository userSessionRepository;
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
	    }
	    
	    @Override
	    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
	        logger.info("Session closed with status {}", status);
	        MeetRoom rmRoom = roomRepository.removeRoomMaps(session.getId());//remove in user information
	        roomRepository.removeInRoom(rmRoom.getRoomId(),session.getId()); //remove in global room
	    }

	    @Override
	    public void afterConnectionEstablished(final WebSocketSession session) {
	    	 logger.info("Session Connect with SessionId {}", session);
	    	 /*
	         sendMessage(session, WebSocketMessage
	        		 				.builder()
	        		 				.from("Server")
	        		 				.type(MSG_TYPE_JOIN)
	        		 				.build());
	          */
	         
	    }

	    @Override
	    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
	        // a message has been received
	    	logger.info("handleTextMessage {}",session);
	    	logger.info("handleTextMessage PayLoad{}",textMessage.getPayload());
	    	
	        try {
	        	 WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	        	 String FromId = message.getFrom();
	        	 String userName = message.getUserName();
	        	 String sessionId = session.getId();
	        	 
	        	 MeetRoom room = null;
	        	 room = roomRepository.getRoomInRoomMaps(FromId);
	        	 
	        	 switch(message.getType()) {
	        	 case  MSG_TYPE_JOIN:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_JOIN Data : {}", message.getData());
	        		 
	        		 String joinRoomId = message.getRoomId();
	        		 
	        		 UserSession uSession = new UserSession();
	        		 uSession.setSessionId(sessionId);
	        		 uSession.setUserName(userName);
	        		 uSession.setSessionType(0);
	        		 
	        		 userSessionRepository.save(uSession);
	        		 
	        		 room = roomRepository.findRoomByStringId(joinRoomId) // 해당 방번호가 있는지 확인
	                            .orElseThrow(() -> new IOException("Invalid room received!"));
	        		 
	        		 roomRepository.putRoomMaps(FromId, room); // Add in user information
	        		 roomRepository.addClient(room, FromId,userName, session); // Add in Global Room
	        		 message.setData(roomRepository.getNameInClients(room));
	        		 
	        		 sendMessage(session, message);
	        		 
	        		 break;
	        	 case MSG_TYPE_OFFER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_OFFER Message : {}",message.getFrom());
	        		 
	        		 if(room != null) {
	        			 sendSDPinOtherSessions(sessionId, room, message);
	                 }
	        		 
	        		 break;
	        	 case MSG_TYPE_ANSWER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ANSWER Message : {}",message.getFrom());
	        		 
	        		 if(room != null) {
	        			 sendSDPinOtherSessions(sessionId, room, message);
	                 }
	        		 
	        		 break;
	        	 case  MSG_TYPE_ICE:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ICE Message : {}",message.getFrom());
	                 
	                 if(room != null) {
	        			 sendSDPinOtherSessions(sessionId, room, message);
	                 }
	                 
	        		 break;
	        	 default:
	        		 logger.info("handleTextMessage XXXX {} {}",message.getType(), session);
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
	    
	    private void sendSDPinOtherSessions(String sessionId, MeetRoom room, WebSocketMessage message) {
       	 Map<String, MeetUserSession> clients = roomRepository.getClients(room); // 해당 룸에 접속한 세션들 가져오기
       	 	for(Map.Entry<String, MeetUserSession> client : clients.entrySet())  { 
       	 		//if (!client.getKey().equals(sessionId)) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
       	 		 if (client.getKey().equals(message.getTo())) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
                 sendMessage(client.getValue().getWebSocketSession(), WebSocketMessage
                		 						.builder()
                		 						.from(message.getFrom())
                		 						.to(message.getTo())
                		 						.type(message.getType())
                		 						.data(message.getData())
                		 						.candidate(message.getCandidate())
                		 						.sdp(message.getSdp())
                		 						.build());
       	 		}
       	 	} 
	    }

	}