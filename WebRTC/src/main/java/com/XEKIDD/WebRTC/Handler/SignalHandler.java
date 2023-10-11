package com.XEKIDD.WebRTC.Handler;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.XEKIDD.WebRTC.Domain.MeetRoom;
import com.XEKIDD.WebRTC.Domain.WebSocketMessage;
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
	        String sessionId = session.getId();
	        Optional<UserSession> uSession = userSessionRepository.findById(sessionId);
	        if(uSession.isPresent()) {
	        	String userName = uSession.get().getUserName();
	        	MeetRoom rmRoom = roomRepository.removeUserMaps(userName); // remove in User Maps
	        	roomRepository.removeInRoom(rmRoom.getRoomId(),userName); // remove in Global Room
	        }
	        userSessionRepository.deleteById(sessionId); // remove in Redis
	    }

	    @Override
	    public void afterConnectionEstablished(final WebSocketSession session) {
	    	 logger.info("Session Connect with SessionId {}", session);
	         
	    }

	    @Override
	    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {

	    	logger.info("handleTextMessage {}",session);
	    	logger.info("handleTextMessage PayLoad{}",textMessage.getPayload());
	    	
	        try {
	        	 WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	        	 String FromId = message.getFrom();
	        	 String userName = message.getUserName();
	        	 String sessionId = session.getId();
	        	 
	        	 MeetRoom room = null;
	        	 room = roomRepository.getRoomInUserMaps(FromId); // get user at room 
	        	 
	        	 switch(message.getType()) {
	        	 case  MSG_TYPE_JOIN:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_JOIN Data : {}", message.getData());
	        		 
	        		 String joinRoomId = message.getRoomId();
	        		 
	        		 //Redis
	        		 UserSession uSession = new UserSession();
	        		 uSession.setSessionId(sessionId);
	        		 uSession.setUserName(FromId);
	        		 uSession.setSessionType(0);
	        		 userSessionRepository.save(uSession);
	        		 
	        		 //Local Map
	        		 room = roomRepository.findRoomByStringId(joinRoomId) // check Exist RoomId
	                            .orElseThrow(() -> new IOException("Invalid room received!"));
	        		 
	        		 roomRepository.putUserMaps(FromId, room); // Add in User Maps
	        		 roomRepository.addClient(room, FromId, session); // Add in Global Room
	        		 
	        		 message.setData(roomRepository.getNameInClients(room)); // list users in Global room
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
       	 Map<String, WebSocketSession> clients = roomRepository.getClients(room); // get Clients in Current Room
       	 	for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  { 
       	 		 if (client.getKey().equals(message.getTo())) { // send Message exclude self
                 sendMessage(client.getValue(), WebSocketMessage
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