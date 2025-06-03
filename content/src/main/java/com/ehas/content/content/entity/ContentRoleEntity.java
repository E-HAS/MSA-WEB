package com.ehas.content.content.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="content_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentRoleEntity {
    @Id
    private Integer seq;
    
	private Integer contentSeq;
	
	@Column(nullable = false, length = 100)
	private String roleName;
	@Column(nullable = false, length = 500)
	private String roleDept;
}
