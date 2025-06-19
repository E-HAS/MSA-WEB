package com.ehas.infra.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="server")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String host;
    
    @Column(length = 200)
    private String dept;
    
    @Column
    private LocalDateTime regDate;
}
