package com.ehas.lotto.order.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.order.dto.OrderDto;
import com.ehas.lotto.order.entity.OrderEntity;
import com.ehas.lotto.order.repository.OrderJpaRepository;
import com.ehas.lotto.order.repository.OrderSpecifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderJpaRepository orderJpaRepository;

    public OrderDto add(OrderDto dto) {
    	dto.setCreatedDate(LocalDateTime.now());
        OrderEntity saved = orderJpaRepository.save(dto.toEntity(dto));
        return  saved.toDto(saved);
    }

    public OrderDto findBySeq(Integer seq) {
        return orderJpaRepository.findById(seq).map(v->v.toDto(v)).orElse(null);
    }

    public List<OrderDto> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(v->v.toDto(v))
                .collect(Collectors.toList());
    }
    
    public Page<OrderDto> findAllBySpecAndPageable(Integer seq
    													, Integer userSeq
    													,Integer status
    													,String stCreatedDate
    													,String endCreatedDate
    													,Pageable pageable) {
    	Specification<OrderEntity> spec = OrderSpecifications.findWith(seq, userSeq,status,stCreatedDate,endCreatedDate);
    	
        return orderJpaRepository.findAll(spec, pageable)
                .map(v->v.toDto(v));
    }

    public void deleteBySeq(Integer seq) {
    	orderJpaRepository.deleteById(seq);
    }
}