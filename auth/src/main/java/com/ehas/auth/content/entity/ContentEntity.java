package com.ehas.auth.content.entity;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="content_users")
public class ContentEntity {
    @Id
    private Integer seq;

    private String contentName;
    private String contentDept;
    private Boolean used;
    private LocalDateTime registeredDate;
    private LocalDateTime updatedDate;
    
    private Integer sortOrder;
}
