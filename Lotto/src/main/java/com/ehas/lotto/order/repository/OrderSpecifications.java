package com.ehas.lotto.order.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.order.entity.OrderEntity;

import jakarta.persistence.criteria.Predicate;

public class OrderSpecifications {
	public static Specification<OrderEntity> findWith(
			Integer seq
		    ,Integer userSeq
		    ,Integer status
		    ,String stCreatedDate
		    ,String enCreatedDate
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seq != null) {
                predicates.add(criteriaBuilder.equal(root.get("seq"), seq));
            }
            
            if (userSeq != null) {
                predicates.add(criteriaBuilder.equal(root.get("userSeq"), userSeq));
            }
            
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (stCreatedDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), stCreatedDate));
            }

            if (enCreatedDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), enCreatedDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
