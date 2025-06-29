package com.ehas.lotto.product.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.ehas.lotto.order.entity.OrderEntity;
import com.ehas.lotto.product.entity.ProductEntity;

import jakarta.persistence.criteria.Predicate;

public class ProductSpecifications {
	public static Specification<ProductEntity> findWith(
			Integer seq
			,Integer user_seq
			, String name
			, Integer status
			, String stCreatedDate
			, String enCreatedDate
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seq != null) {
                predicates.add(criteriaBuilder.equal(root.get("seq"), seq));
            }
            if (user_seq != null) {
                predicates.add(criteriaBuilder.equal(root.get("user_seq"), user_seq));
            }
            
            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), name));
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
