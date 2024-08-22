package com.ehas.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleKey implements Serializable{

    private Integer userType;
    
    private String userId;
    
    private String userRole;
}
