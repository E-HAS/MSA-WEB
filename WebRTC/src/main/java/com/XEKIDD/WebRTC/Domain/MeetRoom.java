package com.XEKIDD.WebRTC.Domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetRoom {
	private final String roomId;
	private String roomName;
	private String roomPasswrd;
    private final Map<String, MeetUserSession> clients = new HashMap<>();
}
