package com.ehas.lotto.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.ehas.lotto.product.entity.ProductEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Integer>, Specification<ProductEntity> {
	Page<ProductEntity> findAll(@Nullable Specification<ProductEntity> spec,Pageable pageable);
}