package com.ehas.content.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="user_role")
@IdClass(UserRoleEntityKey.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = {"user", "role"})
public class UserRoleEntity {
    @Id
	private Integer userSeq;
    @Id
	private Integer roleSeq;
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userSeq", referencedColumnName="seq", insertable = false, updatable = false)//상대 객체 검색 컬럼 name=, 내 객체 검색 컬럼 referencedColumnName=
    private UserEntity user;  
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="roleSeq", referencedColumnName="seq", insertable = false, updatable = false)//상대 객체 검색 컬럼 name=, 내 객체 검색 컬럼 referencedColumnName=
    private RoleEntity role;
    
}
