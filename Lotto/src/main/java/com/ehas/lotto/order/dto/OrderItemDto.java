package com.ehas.lotto.order.dto;
import com.ehas.lotto.order.entity.OrderItemEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Integer seq;
    private Integer orderSeq;
    private Integer productSeq;
    private Integer price;
    private Short one;
    private Short two;
    private Short three;
    private Short four;
    private Short five;
    private Short six;
    
    public OrderItemEntity toEntity(OrderItemDto dto) {
        return OrderItemEntity.builder()
                .seq(dto.getSeq())
                .orderSeq(dto.getOrderSeq())
                .productSeq(dto.getProductSeq())
                .price(dto.getPrice())
                .one(dto.getOne())
                .two(dto.getTwo())
                .three(dto.getThree())
                .four(dto.getFour())
                .five(dto.getFive())
                .six(dto.getSix())
                .build();
    }
}