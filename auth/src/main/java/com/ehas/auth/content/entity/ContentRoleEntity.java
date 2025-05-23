package com.ehas.auth.content.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="content_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentRoleEntity {
    @Id
    private Integer seq;
    
	private Integer contentSeq;
	private String roleName;
	private String roleDept;
}
