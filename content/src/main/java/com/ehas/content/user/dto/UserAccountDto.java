package com.ehas.content.user.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountDto {
    private Integer seq;
    private Integer userSeq;
    private Integer balance;
    private Integer status;
    private LocalDateTime created_date;
}
