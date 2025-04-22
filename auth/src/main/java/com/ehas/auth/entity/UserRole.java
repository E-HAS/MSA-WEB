package com.ehas.auth.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.EmbeddedId;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="userrole")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRole {
	@EmbeddedId
	private UserRoleEntityKey id;
	
    @Column("user_type")
    private Integer userType;

    @Column("user_uid")
    private Integer userUid;

    @Column("user_role")
    private String userRole;
}
