package com.ehas.lotto.order.entity;

import com.ehas.lotto.order.dto.OrderItemDto;
import com.ehas.lotto.product.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Integer seq;

    @Column(name = "order_seq", nullable = false)
    private Integer orderSeq;

    @Column(name = "product_seq", nullable = false)
    private Integer productSeq;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "one", nullable = false)
    private Short one;

    @Column(name = "two", nullable = false)
    private Short two;

    @Column(name = "three", nullable = false)
    private Short three;

    @Column(name = "four", nullable = false)
    private Short four;

    @Column(name = "five", nullable = false)
    private Short five;

    @Column(name = "six", nullable = false)
    private Short six;

    @Column(name = "bonus", nullable = false)
    private Short bonus;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_seq", referencedColumnName="seq", insertable = false, updatable = false)
    private OrderEntity order;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_seq", referencedColumnName="seq", insertable = false, updatable = false)
    private ProductEntity product;
    
    public OrderItemDto toDto(OrderItemEntity entity) {
        return OrderItemDto.builder()
                .seq(entity.getSeq())
                .orderSeq(entity.getOrderSeq())
                .productSeq(entity.getProductSeq())
                .price(entity.getPrice())
                .one(entity.getOne())
                .two(entity.getTwo())
                .three(entity.getThree())
                .four(entity.getFour())
                .five(entity.getFive())
                .six(entity.getSix())
                .bonus(entity.getBonus())
                .build();
    }
}