package com.ehas.lotto.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.ehas.lotto.product.dto.ProductDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Integer seq;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "dept", columnDefinition = "TEXT")
    private String dept;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "created_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime createdDate;

    @Column(name = "updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedDate;
    
    public ProductDto toDto(ProductEntity entity) {
        return ProductDto.builder()
                .seq(entity.getSeq())
                .name(entity.getName())
                .dept(entity.getDept())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }
}