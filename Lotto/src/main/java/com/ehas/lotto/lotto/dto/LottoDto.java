package com.ehas.lotto.lotto.dto;
import com.ehas.lotto.lotto.entity.LottoEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LottoDto {
    private Integer round;
    private Short one;
    private Short two;
    private Short three;
    private Short four;
    private Short five;
    private Short six;
    private Short bonus;
    
	public LottoEntity toEntity(LottoDto dto) {
		return LottoEntity.builder().round(dto.getRound()).one(dto.getOne()).two(dto.getTwo()).three(dto.getThree())
				.four(dto.getFour()).five(dto.getFive()).six(dto.getSix()).bonus(dto.getBonus()).build();
	}
}