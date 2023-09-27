package com.XEKIDD.WebRTC.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class WebSocketMessage {
    private String from;
    private String to;
    
    private String roomId;
    private String userName;
    private String type;
    private String data;
    
    private Object candidate;
    private Object sdp;

}
