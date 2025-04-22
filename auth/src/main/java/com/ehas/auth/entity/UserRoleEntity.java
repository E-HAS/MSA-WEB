package com.ehas.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="userrole")
@IdClass(UserRoleEntityKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleEntity {
	@Id
    @Column(name="user_type",length = 30, nullable = false)
    private Integer userType;
    @Id
    @Column(name="user_uid",length = 30, nullable = false)
    private Integer userUid;
    @Id
    @Column(name="user_role",length = 30, nullable = false)
    private String userRole;
}
