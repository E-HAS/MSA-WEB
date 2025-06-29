package com.ehas.lotto.lotto.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.ehas.lotto.lotto.entity.LottoNumberStatEntity;
import com.ehas.lotto.lotto.entity.LottoNumberStatId;

@Repository
public interface LottoNumberStatJpaRepository extends JpaRepository<LottoNumberStatEntity, LottoNumberStatId>, JpaSpecificationExecutor<LottoNumberStatEntity>  {
	Page<LottoNumberStatEntity> findAll(@Nullable Specification<LottoNumberStatEntity> spec,Pageable pageable);
}