package com.XEKIDD.WebRTC.Domain;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetRoomDto {
	private final String roomId;
	private String roomName;
	private String roomPasswrd;
	private String userName;
}
