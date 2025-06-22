package com.ehas.lotto.lotto.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.lotto.entity.LottoEntity;

import jakarta.persistence.criteria.Predicate;

public class LottoSpecifications {
	public static Specification<LottoEntity> findWith(
		    Integer stRound
		    ,Integer enRound
		    ,Short one
		    ,Short two
		    ,Short three
		    ,Short four
		    ,Short five
		    ,Short six
		    ,Short bonus
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (one != null) {
                predicates.add(criteriaBuilder.equal(root.get("one"), one));
            }
            
            if (two != null) {
                predicates.add(criteriaBuilder.equal(root.get("two"), two));
            }
            
            if (three != null) {
                predicates.add(criteriaBuilder.equal(root.get("three"), three));
            }

            if (four != null) {
                predicates.add(criteriaBuilder.equal(root.get("four"), four));
            }

            if (five != null) {
                predicates.add(criteriaBuilder.equal(root.get("five"), five));
            }

            if (six != null) {
                predicates.add(criteriaBuilder.equal(root.get("six"), six));
            }

            if (bonus != null) {
                predicates.add(criteriaBuilder.equal(root.get("bonus"), bonus));
            }

            if (stRound != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("round"), stRound));
            }

            if (enRound != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("round"), enRound));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
