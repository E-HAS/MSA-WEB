package com.ehas.lotto.lotto.dto;

import com.ehas.lotto.lotto.entity.LottoRangeStatEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LottoRangeStatDto {
    private Integer number;
    private Short one;
    private Short two;
    private Short three;
    private Short four;
    private Short five;
    private Short six;
    private Integer sum;
    private Float percent;
    
    public LottoRangeStatEntity toEntity(LottoRangeStatDto dto) {
        return LottoRangeStatEntity.builder()
                .number(dto.getNumber())
                .one(dto.getOne())
                .two(dto.getTwo())
                .three(dto.getThree())
                .four(dto.getFour())
                .five(dto.getFive())
                .six(dto.getSix())
                .sum(dto.getSum())
                .percent(dto.getPercent())
                .build();
    }
}