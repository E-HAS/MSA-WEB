package com.xekidd.stomp.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class StomeRoomMessageEntityPK implements Serializable{
	LocalDateTime reqDt;
	String roomId;
}
