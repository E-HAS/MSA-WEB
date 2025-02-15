package com.ehas.auth.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="User")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User{
    @Id
    private Integer uid;

    @Column("user_type")
    private Integer userType;
    @Column("user_id")
    private String userId;
    @Column("user_password")
    private String userPassword;
    
    @Column("nick_name")
    private String nickName;
    
    @Column("user_state")
    private String userState; // Y : 정상 회원 , L : 잠긴 계정, P : 패스워드 만료, A : 계정 만료
    
    @Column("registered_date")
    @CreatedDate
    private LocalDateTime registeredDate;
    
    @Column("updated_date")
    @LastModifiedDate
    private LocalDateTime updatedDate;
    
    @Column("deleted_date")
    private LocalDateTime deletedDate;
    @Column("password_updated_date")
    private LocalDateTime passwordUpdatedDate;
}