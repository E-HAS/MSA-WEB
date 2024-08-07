package com.ehas.auth.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="userrole")
@JsonIgnoreProperties({"userType","userId","userRole"})
@IdClass(UserRoleKey.class)
public class UserRole {
	@Id
    @Column(name="user_type", length = 30, nullable = false)
    private Integer userType;
    
	@Id
    @Column(name="user_id", length = 30, nullable = false)
    private String userId;
    
	@Id
    @Column(name="user_role", length = 30, nullable = false)
    private String userRole;
}
