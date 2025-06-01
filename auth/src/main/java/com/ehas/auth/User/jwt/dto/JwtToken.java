package com.ehas.auth.User.jwt.dto;

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
public class JwtToken {
	private String prefix;
	private String accessToken;
	private String RefeshToken;
}
