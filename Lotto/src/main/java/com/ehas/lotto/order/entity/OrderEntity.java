package com.ehas.lotto.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ehas.lotto.order.dto.OrderDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Integer seq;

    @Column(name = "user_seq", nullable = false)
    private Integer userSeq;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "created_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime createdDate;

    @Column(name = "updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedDate;
    
    @JsonIgnore
    @Builder.Default
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "order") // mappedBy= 프로퍼티 이름
    private List<OrderItemEntity> items = new ArrayList<OrderItemEntity>();
    
    public OrderDto toDto(OrderEntity entity) {
        return OrderDto.builder()
                .seq(entity.getSeq())
                .userSeq(entity.getUserSeq())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .totalPrice(entity.getTotalPrice())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}