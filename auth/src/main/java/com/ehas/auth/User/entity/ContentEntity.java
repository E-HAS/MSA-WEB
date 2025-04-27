package com.ehas.auth.User.entity;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="content")
public class ContentEntity {
    @Id
    private Integer seq;

    private String contentId;
    private String contentDept;
    
}
