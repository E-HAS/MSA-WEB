package com.ehas.lotto.lotto.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.lotto.entity.LottoRangeStatEntity;

import jakarta.persistence.criteria.Predicate;

public class LottoRangeStatSpecifications {
	public static Specification<LottoRangeStatEntity> findWith(
		    Integer number
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

            if (number != null) {
                predicates.add(criteriaBuilder.equal(root.get("number"), number));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
