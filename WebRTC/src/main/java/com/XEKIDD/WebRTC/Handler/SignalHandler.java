package com.XEKIDD.WebRTC.Handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.XEKIDD.WebRTC.Domain.Room;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
public class SignalHandler extends TextWebSocketHandler {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	 private final ObjectMapper objectMapper = new ObjectMapper();

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

	    @Override
	    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
	        logger.debug("[ws] Session has been closed with status {}", status);
	        sessionIdToRoomMap.remove(session.getId());
	    }

	    @Override
	    public void afterConnectionEstablished(final WebSocketSession session) {
	        // webSocket has been opened, send a message to the client
	        // when data field contains 'true' value, the client starts negotiating
	        // to establish peer-to-peer connection, otherwise they wait for a counterpart
	    	 logger.info(">>>>>>>>>>>>>>>>{}, {}, {}", session.getId(), session.getLocalAddress(), session.getRemoteAddress());
	    	 logger.info(">>>>>>>>>>>>>>>>{}", session);
	        sendMessage(session, "afterConnectionEstablished");
	    }

	    @Override
	    protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
	        // a message has been received
	    	logger.info("handleTextMessage {}",session);
	    	logger.info("handleTextMessage {}",textMessage);
	    	/*
	        try {
	        	logger.info("handleTextMessage {}",session);
	            //WebSocketMessage message = objectMapper.readValue(textMessage.getPayload(), WebSocketMessage.class);
	            //logger.debug("[ws] Message of {} type from {} PayLoad received", message.getType(), message.getPayload());

	        } catch (IOException e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }
	        */
	    }

	    private void sendMessage(WebSocketSession session,String message) {
	        try {
	            String json = objectMapper.writeValueAsString(message);
	            session.sendMessage(new TextMessage(json));
	        } catch (IOException e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }
	    }
	}