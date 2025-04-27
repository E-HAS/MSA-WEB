package com.ehas.auth.User.entity;


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
@Table(name ="userrole")
//@IdClass(UserRoleEntityKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleEntity {
    @Id
	private Integer contentSeq;
    @Id
	private Integer userSeq;
    @Id
	private Integer roleSeq;
}
