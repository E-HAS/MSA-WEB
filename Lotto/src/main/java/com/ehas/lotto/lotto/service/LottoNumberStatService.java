package com.ehas.lotto.lotto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.lotto.dto.LottoNumberStatDto;
import com.ehas.lotto.lotto.entity.LottoNumberStatEntity;
import com.ehas.lotto.lotto.entity.LottoNumberStatId;
import com.ehas.lotto.lotto.repository.LottoNumberStatJpaRepository;
import com.ehas.lotto.lotto.repository.LottoRangeStatSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LottoNumberStatService {
	private final LottoNumberStatJpaRepository lottoNumberStatJpaRepository;

    public LottoNumberStatDto add(LottoNumberStatDto dto) {
    	LottoNumberStatEntity saved = lottoNumberStatJpaRepository.save(dto.toEntity(dto));
        return saved.toDto(saved);
    }

    public LottoNumberStatDto findByNumber(Integer round, Integer number) {
        return lottoNumberStatJpaRepository.findById(LottoNumberStatId.builder()
        																.round(round)
        																.number(number)
        																.build())
        		.map(v->v.toDto(v)).orElse(null);
    }

    public List<LottoNumberStatDto> findAll() {
        return lottoNumberStatJpaRepository.findAll().stream()
                .map(v->v.toDto(v))
                .collect(Collectors.toList());
    }
    
    public Page<LottoNumberStatDto> findAllBySpecAndPageable(Integer stRound,
    														Integer enRound,
    														Integer number,
															Pageable pageable) {
    	Specification<LottoNumberStatEntity> spec = LottoRangeStatSpecifications.findWith(stRound,enRound,number
				,null,null,null,null,null,null,null);
    	
        return lottoNumberStatJpaRepository.findAll(spec,pageable)
                .map(v->v.toDto(v));
    }

    public void deleteByNumber(Integer round, Integer number) {
    	lottoNumberStatJpaRepository.deleteById(LottoNumberStatId.builder()
																.round(round)
																.number(number)
																.build());
    }
}
