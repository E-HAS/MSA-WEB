package com.ehas.auth.content.entity;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import com.ehas.auth.User.userstatus.UserStatus;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="content_user")
public class ContentUserEntity {
    @Id
    private Integer seq;
    private Integer contentSeq;
    private Integer userSeq;

    private String userName;
    private String userDept;
    private UserStatus status;
    private LocalDateTime registeredDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deleted_date;
}
