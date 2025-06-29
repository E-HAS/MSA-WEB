package com.ehas.lotto.order.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.order.dto.OrderItemDto;
import com.ehas.lotto.order.entity.OrderItemEntity;
import com.ehas.lotto.order.repository.OrderItemJpaRepository;
import com.ehas.lotto.order.repository.OrderItemSpecifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
	private final OrderItemJpaRepository orderItemJpaRepository;
	 
    public OrderItemDto add(OrderItemDto dto) {
        OrderItemEntity saved = orderItemJpaRepository.save(dto.toEntity(dto));
        return saved.toDto(saved);
    }

    public OrderItemDto findBySeq(Integer seq) {
        return orderItemJpaRepository.findById(seq).map(v->v.toDto(v)).orElse(null);
    }

    public List<OrderItemDto> findAll() {
        return orderItemJpaRepository.findAll().stream()
                .map(v->v.toDto(v))
                .collect(Collectors.toList());
    }

    public Page<OrderItemDto> findAllBySpecAndPageable(Integer seq,Integer orderSeq,Integer productSeq,Pageable pageable) {
    	
    	Specification<OrderItemEntity> spec =  OrderItemSpecifications.findWith(seq,orderSeq,productSeq);
        return orderItemJpaRepository.findAll(spec,pageable)
                .map(v->v.toDto(v));
    }
    
    public void deleteBySeq(Integer seq) {
    	orderItemJpaRepository.deleteById(seq);
    }
}
