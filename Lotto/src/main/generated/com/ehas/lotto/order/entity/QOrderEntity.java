package com.ehas.lotto.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderEntity is a Querydsl query type for OrderEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderEntity extends EntityPathBase<OrderEntity> {

    private static final long serialVersionUID = 1726735092L;

    public static final QOrderEntity orderEntity = new QOrderEntity("orderEntity");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final ListPath<OrderItemEntity, QOrderItemEntity> items = this.<OrderItemEntity, QOrderItemEntity>createList("items", OrderItemEntity.class, QOrderItemEntity.class, PathInits.DIRECT2);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final NumberPath<Integer> totalAmount = createNumber("totalAmount", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedDate = createDateTime("updatedDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userSeq = createNumber("userSeq", Integer.class);

    public QOrderEntity(String variable) {
        super(OrderEntity.class, forVariable(variable));
    }

    public QOrderEntity(Path<? extends OrderEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderEntity(PathMetadata metadata) {
        super(OrderEntity.class, metadata);
    }

}

