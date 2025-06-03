package com.ehas.content.content.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentRoleDto {
    private Integer seq;
    
	private Integer contentSeq;
	private String roleName;
	private String roleDept;
}
