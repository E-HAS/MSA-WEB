package com.ehas.auth.User.dto;

import com.ehas.auth.User.userstatus.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private Integer seq;
	private String id;
	private String name;
	private String password;
	private Integer status;
	
	private Integer addressSeq;
	private Integer roleSeq;
}
