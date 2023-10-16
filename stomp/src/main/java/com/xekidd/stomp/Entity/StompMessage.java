package com.xekidd.stomp.Entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class StompMessage {
    private String roomId;
    private String type;
	private String from;
	private String to;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Object data;
	 
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object candidate;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object sdp;
}
