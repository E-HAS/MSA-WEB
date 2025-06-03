package com.ehas.content.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentUserRoleDto {
	
	private Integer userSeq;
	private Integer contentSeq;
	private Integer contentRoleSeq;
}
