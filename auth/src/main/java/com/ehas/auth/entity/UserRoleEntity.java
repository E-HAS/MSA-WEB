package com.ehas.auth.entity;


import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="userrole")
@IdClass(UserRoleKey.class)
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
