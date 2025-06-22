package com.ehas.lotto.product.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.ehas.lotto.product.entity.ProductEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Integer seq;
    private String name;
    private String dept;
    private Integer price;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    public ProductEntity toEntity(ProductDto dto) {
        return ProductEntity.builder()
                .seq(dto.getSeq())
                .name(dto.getName())
                .dept(dto.getDept())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .createdDate(dto.getCreatedDate())
                .updatedDate(dto.getUpdatedDate())
                .build();
    }
}