package com.ehas.lotto.product.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ehas.lotto.product.dto.ProductDto;
import com.ehas.lotto.product.entity.ProductEntity;
import com.ehas.lotto.product.repository.ProductJpaRepository;
import com.ehas.lotto.product.repository.ProductSpecifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductJpaRepository productJpaRepository;

    public ProductDto add(ProductDto dto) {
    	dto.setCreatedDate(LocalDateTime.now());
        ProductEntity saved = productJpaRepository.save(dto.toEntity(dto));
        return saved.toDto(saved);
    }
    public ProductDto findBySeq(Integer seq) {
        Optional<ProductEntity> entity = productJpaRepository.findById(seq);
        return entity.map(v->v.toDto(v)).orElse(null);
    }
    
    public List<ProductDto> findAll() {
        return productJpaRepository.findAll()
                .stream()
                .map(v->v.toDto(v))
                .collect(Collectors.toList());
    }
    
    public Page<ProductDto> findAllBySpecAndPageable(Integer seq, Integer user_seq, String name, Integer status, String stCreatedDate, String enCreatedDate, Pageable pageable) {
    	Specification<ProductEntity> spec = ProductSpecifications.findWith(seq, user_seq, name, status, stCreatedDate, enCreatedDate);
    	return productJpaRepository.findAll(spec, pageable)
                .map(v->v.toDto(v));
    }
    
    public void deleteBySeq(Integer seq) {
    	productJpaRepository.deleteById(seq);
    }
}
