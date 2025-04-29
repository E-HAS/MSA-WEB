package com.ehas.auth.content.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="contentrole")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentRoleEntity {
    @Id
	private Integer contentSeq;
    @Id
	private Integer roleSeq;
}
