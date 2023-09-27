package com.XEKIDD.WebRTC.Domain;

import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetUserSession {
	private String UserName;
	private WebSocketSession webSocketSession;
}
