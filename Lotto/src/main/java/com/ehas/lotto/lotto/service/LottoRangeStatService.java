package com.ehas.lotto.lotto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.lotto.dto.LottoRangeStatDto;
import com.ehas.lotto.lotto.entity.LottoRangeStatEntity;
import com.ehas.lotto.lotto.repository.LottoRangeStatJpaRepository;
import com.ehas.lotto.lotto.repository.LottoRangeStatSpecifications;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LottoRangeStatService {
	private final LottoRangeStatJpaRepository lottoRangeStatJpaRepository;

    public LottoRangeStatDto add(LottoRangeStatDto dto) {
    	LottoRangeStatEntity saved = lottoRangeStatJpaRepository.save(dto.toEntity(dto));
        return saved.toDto(saved);
    }

    public LottoRangeStatDto findByNumber(Integer number) {
        return lottoRangeStatJpaRepository.findById(number)
        		.map(v->v.toDto(v)).orElse(null);
    }

    public List<LottoRangeStatDto> findAll() {
        return lottoRangeStatJpaRepository.findAll().stream()
                .map(v->v.toDto(v))
                .collect(Collectors.toList());
    }
    
    public Page<LottoRangeStatDto> findAllBySpecAndPageable(Integer number,
															Pageable pageable) {
    	Specification<LottoRangeStatEntity> spec = LottoRangeStatSpecifications.findWith(number
				,null,null,null,null,null,null,null);
    	
        return lottoRangeStatJpaRepository.findAll(spec,pageable)
                .map(v->v.toDto(v));
    }

    public void deleteByNumber(Integer number) {
    	lottoRangeStatJpaRepository.deleteById(number);
    }
}
