package com.ehas.auth.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="userrole")
//@IdClass(UserRoleKey.class)
public class UserRole {
    @Column(value="user_type")
    private Integer userType;
    
    @Column(value="user_id")
    private String userId;
    
    @Column(value="user_role")
    private String userRole;
}
