package com.ehas.lotto.lotto.entity;

import com.ehas.lotto.lotto.dto.LottoRangeStatDto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lotto_range_stat")
public class LottoRangeStatEntity {

    @Id
    @Column(name = "number")
    private Integer number;

    @Column(name = "one")
    private Short one;

    @Column(name = "two")
    private Short two;

    @Column(name = "three")
    private Short three;

    @Column(name = "four")
    private Short four;

    @Column(name = "five")
    private Short five;

    @Column(name = "six")
    private Short six;

    @Column(name = "sum")
    private Integer sum;

    @Column(name = "percent")
    private Float percent;
    
    public LottoRangeStatDto toDto(LottoRangeStatEntity entity) {
        return LottoRangeStatDto.builder()
                .number(entity.getNumber())
                .one(entity.getOne())
                .two(entity.getTwo())
                .three(entity.getThree())
                .four(entity.getFour())
                .five(entity.getFive())
                .six(entity.getSix())
                .sum(entity.getSum())
                .percent(entity.getPercent())
                .build();
    }
}