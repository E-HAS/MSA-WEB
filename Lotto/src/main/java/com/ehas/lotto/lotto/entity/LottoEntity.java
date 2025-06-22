package com.ehas.lotto.lotto.entity;
import com.ehas.lotto.lotto.dto.LottoDto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "lotto")
public class LottoEntity {

	@Id
    @Column(name = "round")
    private Integer round;

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

    @Column(name = "bonus")
    private Short bonus;
    
    public LottoDto toDto(LottoEntity entity) {
		return LottoDto.builder().round(entity.getRound()).one(entity.getOne()).two(entity.getTwo())
				.three(entity.getThree()).four(entity.getFour()).five(entity.getFive()).six(entity.getSix())
				.bonus(entity.getBonus()).build();
	}
}