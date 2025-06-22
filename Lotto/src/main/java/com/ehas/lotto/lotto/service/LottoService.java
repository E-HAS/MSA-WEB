package com.ehas.lotto.lotto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.lotto.dto.LottoDto;
import com.ehas.lotto.lotto.entity.LottoEntity;
import com.ehas.lotto.lotto.repository.LottoJpaRepository;
import com.ehas.lotto.lotto.repository.LottoSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LottoService {
	private final LottoJpaRepository lottoJpaRepository;

	public LottoDto add(LottoDto dto) {
		LottoEntity saved = lottoJpaRepository.save(
				LottoEntity.builder()
				.round(dto.getRound())
				.one(dto.getOne())
				.two(dto.getTwo())
				.three(dto.getThree())
				.four(dto.getFour())
				.five(dto.getFive())
				.six(dto.getSix())
				.bonus(dto.getBonus())
				.build());
		return saved.toDto(saved);
	}

	public LottoDto findByRound(Integer round) {
		return lottoJpaRepository.findById(round).map(v->v.toDto(v)).orElse(null);
	}

	public List<LottoDto> findAll() {
		return lottoJpaRepository.findAll().stream().map(v->v.toDto(v)).collect(Collectors.toList());
	}
	
	public Page<LottoDto> findAllBySpecAndPageable(Integer stRound, 
													Integer enRound,
													Pageable pageable) {
		
		Specification<LottoEntity> spec = LottoSpecifications.findWith(stRound, enRound
																		,null,null,null,null,null,null,null);
		
		return lottoJpaRepository.findAll(spec,pageable).map(v->v.toDto(v));
	}

	public void deleteByRound(Integer round) {
		lottoJpaRepository.deleteById(round);
	}
}
