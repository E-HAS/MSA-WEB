package com.ehas.content.user.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name ="user_account")
public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(name = "user_seq")
    private Integer userSeq;
    
    @Column(name = "balance")
    private Integer balance;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "created_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime created_date;
    
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userSeq", referencedColumnName="seq", insertable = false, updatable = false)
    private UserEntity user;  
}
