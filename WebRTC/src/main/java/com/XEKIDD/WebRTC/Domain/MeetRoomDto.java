package com.XEKIDD.WebRTC.Domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MeetRoomDto {
	private String roomId;
	private String roomName;
	private String roomPassword;
	private String userName;
}
