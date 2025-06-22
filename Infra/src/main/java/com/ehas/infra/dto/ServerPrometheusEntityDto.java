package com.ehas.infra.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerPrometheusEntityDto {
    private Integer seq;
    private String label;
    private String opt;
    private String dept;
    private LocalDateTime regDate;
}
