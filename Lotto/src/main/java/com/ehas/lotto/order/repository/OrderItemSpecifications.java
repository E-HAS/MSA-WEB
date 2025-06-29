package com.ehas.lotto.order.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.order.entity.OrderEntity;
import com.ehas.lotto.order.entity.OrderItemEntity;

import jakarta.persistence.criteria.Predicate;

public class OrderItemSpecifications {
	public static Specification<OrderItemEntity> findWith(
			Integer seq
		    ,Integer orderSeq
		    ,Integer productSeq
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seq != null) {
                predicates.add(criteriaBuilder.equal(root.get("seq"), seq));
            }
            
            if (orderSeq != null) {
                predicates.add(criteriaBuilder.equal(root.get("orderSeq"), orderSeq));
            }
            
            if (productSeq != null) {
                predicates.add(criteriaBuilder.equal(root.get("productSeq"), productSeq));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
