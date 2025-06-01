package com.ehas.content.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
	private Integer seq;
	private String id;
	private String name;
	private String password;
	private Integer status;
	
	private Integer addressSeq;
	private Integer roleSeq;
	
	public interface UserData {
	    String getSeq();
	    String getId();
	    String getStatus();
	    String getAddressSeq();
	    String getroleSeq();
	}
}
