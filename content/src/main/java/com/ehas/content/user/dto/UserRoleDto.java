package com.ehas.content.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleDto {
	private Integer userSeq;
	private Integer roleSeq;
    private String roleName;
    private String roleDept;
}
