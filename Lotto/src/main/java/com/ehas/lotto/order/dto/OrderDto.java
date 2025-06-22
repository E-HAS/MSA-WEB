package com.ehas.lotto.order.dto;

import lombok.*;

import java.time.LocalDateTime;

import com.ehas.lotto.order.entity.OrderEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Integer seq;
    private Integer userSeq;
    private Integer status;
    private Integer totalAmount;
    private Integer totalPrice;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    public OrderEntity toEntity(OrderDto dto) {
        return OrderEntity.builder()
                .seq(dto.getSeq())
                .userSeq(dto.getUserSeq())
                .status(dto.getStatus())
                .totalAmount(dto.getTotalAmount())
                .totalPrice(dto.getTotalPrice())
                .createdDate(dto.getCreatedDate())
                .updatedDate(dto.getUpdatedDate())
                .build();
    }
}