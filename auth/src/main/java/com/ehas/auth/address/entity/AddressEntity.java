package com.ehas.auth.address.entity;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import com.ehas.auth.User.userstatus.UserStatus;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="address")
public class AddressEntity {
    @Id
    private Integer seq;
    private String addressName;
    
    private Integer sidoCode;
    private Integer gugunCode;
    private Integer dongCode;
    private Integer riCode;
    
}
