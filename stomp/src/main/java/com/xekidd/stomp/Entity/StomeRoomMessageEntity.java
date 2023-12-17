package com.xekidd.stomp.Entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "roommessage")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@IdClass(StomeRoomMessageEntityPK.class)
public class StomeRoomMessageEntity {
	@Id
	LocalDateTime reqDt;
	@Id
	String roomId;
	
	String userName;
	String userAddress;
	String roomMessage;
}
