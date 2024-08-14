package com.ehas.auth.dto;

import java.util.List;

import com.ehas.auth.entity.UserEntity;
import com.ehas.auth.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private int userType;
	private int userName;
	private String userId;
	private String userPassword;
}
