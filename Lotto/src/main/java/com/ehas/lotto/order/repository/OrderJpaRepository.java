package com.ehas.lotto.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.ehas.lotto.order.entity.OrderEntity;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Integer> , JpaSpecificationExecutor<OrderEntity>{
    @EntityGraph(attributePaths = {
            "items"
        })
    Page<OrderEntity> findAll(@Nullable Specification<OrderEntity> spec,Pageable pageable);
}
