package com.ehas.lotto.lotto.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.ehas.lotto.lotto.entity.LottoEntity;

@Repository
public interface LottoJpaRepository  extends JpaRepository<LottoEntity, Integer>, JpaSpecificationExecutor<LottoEntity> {
    Page<LottoEntity> findAll(@Nullable Specification<LottoEntity> spec,Pageable pageable);
}