package com.ehas.lotto.lotto.dto;

import com.ehas.lotto.lotto.entity.LottoNumberStatEntity;
import com.ehas.lotto.lotto.entity.LottoNumberStatId;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LottoNumberStatDto {
	private Integer round;
    private Integer number;
    private Short one;
    private Short two;
    private Short three;
    private Short four;
    private Short five;
    private Short six;
    private Integer sum;
    private Float percent;
    
    public LottoNumberStatEntity toEntity(LottoNumberStatDto dto) {
        return LottoNumberStatEntity.builder()
        		.id(LottoNumberStatId.builder()
        								.round(dto.round)
        								.number(dto.number)
        								.build())
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