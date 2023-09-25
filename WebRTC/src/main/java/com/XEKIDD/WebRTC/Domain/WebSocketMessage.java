package com.XEKIDD.WebRTC.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String from;
    private String type;
    private String data;
    private Object candidate;
    private Object sdp;

}
