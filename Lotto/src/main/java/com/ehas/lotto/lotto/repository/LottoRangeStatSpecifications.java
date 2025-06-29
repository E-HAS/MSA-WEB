package com.ehas.lotto.lotto.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.lotto.entity.LottoNumberStatEntity;

import jakarta.persistence.criteria.Predicate;

public class LottoRangeStatSpecifications {
	public static Specification<LottoNumberStatEntity> findWith(
			Integer stRound
			,Integer enRound
			,Integer number
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
            if (stRound != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("round"), stRound));
            }

            if (enRound != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("round"), enRound));
            }
            if (number != null) {
                predicates.add(criteriaBuilder.equal(root.get("number"), number));
            }
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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
