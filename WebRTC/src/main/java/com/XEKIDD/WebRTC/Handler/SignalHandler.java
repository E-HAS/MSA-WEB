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
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class SignalHandler extends TextWebSocketHandler {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	 private final ObjectMapper objectMapper = new ObjectMapper();

	 private Set<Room> rooms = new TreeSet<Room>(Comparator.comparing(Room::getId));
	 
	 private Map<String, Room> sessionIdToRoomMap = new HashMap<>();
	 
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
	    	rooms.add(new Room(1L));
	    }
	    
	    @Override
	    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
	        logger.info("[ws] Session has been closed with status {}", status);
	        sessionIdToRoomMap.remove(session.getId());
	    }

	    @Override
	    public void afterConnectionEstablished(final WebSocketSession session) {
	        // webSocket has been opened, send a message to the client
	        // when data field contains 'true' value, the client starts negotiating
	        // to establish peer-to-peer connection, otherwise they wait for a counterpart
	    	 logger.info(">>>>>>>>>>>>>>>>{}, {}, {}", session.getId(), session.getLocalAddress(), session.getRemoteAddress());
	    	 logger.info(">>>>>>>>>>>>>>>>{}", session);
	         sendMessage(session, new WebSocketMessage("Server", MSG_TYPE_JOIN, Boolean.toString(!sessionIdToRoomMap.isEmpty()), null, null));
	         
	    }

	    @Override
	    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
	        // a message has been received
	    	logger.info("handleTextMessage {}",session);
	    	logger.info("handleTextMessage PayLoad{}",textMessage.getPayload());
	    	
	        try {
	        	 WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	        	 String userName = message.getFrom();
	        	 String data = message.getData();
	        	 
	        	 Room room = null;
	        	 switch(message.getType()) {
	        	 case  MSG_TYPE_JOIN:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_JOIN {}",session);
	        		 
	        		 room = findRoomByStringId(data) // 해당 방번호가 있는지 확인
	                            .orElseThrow(() -> new IOException("Invalid room number received!"));
	        		 
	        		 addClient(room, userName, session); // 해당 방에 이름, 세션 저장 
	        		 sessionIdToRoomMap.put(session.getId(), room); // 해당 세션이 들어가있는 방 정보 추가
	        		 break;
	        	 case MSG_TYPE_OFFER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_OFFER {}",session);
	        		 
	        		 Room rm1 = sessionIdToRoomMap.get(session.getId());
	        		 if(rm1 != null) {
	                	 Map<String, WebSocketSession> clients = getClients(rm1); // 해당 룸에 접속한 세션들 가져오기
	                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  { 
	                            if (!client.getKey().equals(userName)) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
	                            	logger.info("MSG_TYPE_OFFER userName {}",client.getKey());
	                                sendMessage(client.getValue(),
	                                        new WebSocketMessage(
	                                                userName,
	                                                message.getType(),
	                                                data,
	                                                message.getCandidate(),
	                                                message.getSdp()));
	                            }
	                        } 
	                 }
	        		 
	        		 break;
	        	 case MSG_TYPE_ANSWER:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ANSWER {}",session);
	        		 
	        		 Room rm2 = sessionIdToRoomMap.get(session.getId());
	        		 if(rm2 != null) {
	                	 Map<String, WebSocketSession> clients = getClients(rm2); // 해당 룸에 접속한 세션들 가져오기
	                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  { 
	                            if (!client.getKey().equals(userName)) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
	                            	logger.info("MSG_TYPE_ANSWER userName {}",client.getKey());
	                                sendMessage(client.getValue(),
	                                        new WebSocketMessage(
	                                                userName,
	                                                message.getType(),
	                                                data,
	                                                message.getCandidate(),
	                                                message.getSdp()));
	                            }
	                        } 
	                 }
	        		 
	        		 break;
	        	 case  MSG_TYPE_ICE:
	        		 logger.info("handleTextMessage >>>> MSG_TYPE_ICE {}",session);
	        		 Object candidate = message.getCandidate();
	                 Object sdp = message.getSdp();
	                 logger.info("handleTextMessage >>>> candidate {}",candidate.toString());
	                 //logger.info("handleTextMessage >>>> sdp {}",sdp.toString());
	                 
	                 Room rm = sessionIdToRoomMap.get(session.getId()); // 해당 세션이 들어간 룸 정보 가져오기
	                 if(rm != null) {
	                	 Map<String, WebSocketSession> clients = getClients(rm); // 해당 룸에 접속한 세션들 가져오기
	                        for(Map.Entry<String, WebSocketSession> client : clients.entrySet())  { 
	                            if (!client.getKey().equals(userName)) { // 룸에 자신 세션 제외한 나머지 메시지 전송(signal)
	                            	logger.info("MSG_TYPE_ICE userName {}",client.getKey());
	                            	
	                                sendMessage(client.getValue(),
	                                        new WebSocketMessage(
	                                                userName,
	                                                message.getType(),
	                                                data,
	                                                candidate,
	                                                sdp));
	                            }
	                        } 
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

	    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
	        try {
	            String json = objectMapper.writeValueAsString(message);
	            session.sendMessage(new TextMessage(json));
	        } catch (IOException e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }
	    }
	    
	    
	    
	    
	    public Set<Room> getRooms() {
	        final TreeSet<Room> defensiveCopy = new TreeSet<>(Comparator.comparing(Room::getId));
	        defensiveCopy.addAll(rooms);

	        return defensiveCopy;
	    }

	    public Boolean addRoom(final Room room) {
	        return rooms.add(room);
	    }

	    public Optional<Room> findRoomByStringId(final String sid) {
	        // simple get() because of parser errors handling
	        return rooms.stream().filter(r -> r.getId().equals(parseId(sid).get())).findAny();
	    }

	    public Long getRoomId(Room room) {
	        return room.getId();
	    }

	    public Map<String, WebSocketSession> getClients(final Room room) {
	        return Optional.ofNullable(room)
	                .map(r -> Collections.unmodifiableMap(r.getClients())) // read-only 객체 생성
	                .orElse(Collections.emptyMap());
	    }

	    public WebSocketSession addClient(final Room room, final String name, final WebSocketSession session) {
	        return room.getClients().put(name, session);
	    }

	    public WebSocketSession removeClientByName(final Room room, final String name) {
	        return room.getClients().remove(name);
	    }
	    
	    public Optional<Long> parseId(String sid) {
	        Long id = null;
	        try {
	            id = Long.valueOf(sid);
	        } catch (Exception e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }

	        return Optional.ofNullable(id);
	    }
	}