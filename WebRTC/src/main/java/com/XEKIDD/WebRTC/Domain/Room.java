package com.XEKIDD.WebRTC.Domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import lombok.Data;

@Data
public class Room {
	private final String id;
	private String roomName;
    private final Map<String, WebSocketSession> clients = new HashMap<>();
}
