package com.ehas.auth.content.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDto {
    private Integer seq;
    private String contentName;
    private String contentDept;
    private Boolean used;
    private LocalDateTime registeredDate;
    private LocalDateTime updatedDate;
}
