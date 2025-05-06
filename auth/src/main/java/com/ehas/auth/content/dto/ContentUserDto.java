package com.ehas.auth.content.dto;

import java.time.LocalDateTime;

import com.ehas.auth.User.userstatus.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentUserDto {
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
