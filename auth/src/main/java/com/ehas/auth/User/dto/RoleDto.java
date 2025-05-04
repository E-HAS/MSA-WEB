package com.ehas.auth.User.dto;

import com.ehas.auth.User.entity.RoleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
	private Integer seq;
    private String roleName;
    private String roleDept;
}
