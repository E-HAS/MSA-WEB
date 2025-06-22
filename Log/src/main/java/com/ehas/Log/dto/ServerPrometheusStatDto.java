package com.ehas.Log.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerPrometheusStatDto {
    private Integer seq;
    private LocalDateTime regDate;
    private Integer second;
    private Integer serverSeq;
    private Integer serverPrometheusSeq;
    private Double value;
}