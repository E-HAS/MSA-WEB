package com.ehas.auth.jwt.dto;

import com.ehas.auth.User.dto.UserDto;
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
public class JwtUserDto {
	private Integer seq;
	private String id;
	private String name;
	private String password;
	
	private Integer addressSeq;
	private Integer roleSeq;
	
}
