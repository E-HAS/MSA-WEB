package com.ehas.Log.entity;
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
@Table(name = "server_prometheus_stat")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerPrometheusStatEntity {
    @Id
    @Column
    private Integer seq;
    
    @Column
    private LocalDateTime regDate;

    @Column
    private Integer second;

    @Column
    private Integer serverSeq;

    @Column
    private Integer serverPrometheusSeq;

    @Column
    private Double value;
}