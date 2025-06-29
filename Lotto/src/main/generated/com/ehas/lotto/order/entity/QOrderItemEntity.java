package com.ehas.lotto.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderItemEntity is a Querydsl query type for OrderItemEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItemEntity extends EntityPathBase<OrderItemEntity> {

    private static final long serialVersionUID = 791183207L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderItemEntity orderItemEntity = new QOrderItemEntity("orderItemEntity");

    public final NumberPath<Short> bonus = createNumber("bonus", Short.class);

    public final NumberPath<Short> five = createNumber("five", Short.class);

    public final NumberPath<Short> four = createNumber("four", Short.class);

    public final NumberPath<Short> one = createNumber("one", Short.class);

    public final QOrderEntity order;

    public final NumberPath<Integer> orderSeq = createNumber("orderSeq", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.ehas.lotto.product.entity.QProductEntity product;

    public final NumberPath<Integer> productSeq = createNumber("productSeq", Integer.class);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final NumberPath<Short> six = createNumber("six", Short.class);

    public final NumberPath<Short> three = createNumber("three", Short.class);

    public final NumberPath<Short> two = createNumber("two", Short.class);

    public QOrderItemEntity(String variable) {
        this(OrderItemEntity.class, forVariable(variable), INITS);
    }

    public QOrderItemEntity(Path<? extends OrderItemEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderItemEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderItemEntity(PathMetadata metadata, PathInits inits) {
        this(OrderItemEntity.class, metadata, inits);
    }

    public QOrderItemEntity(Class<? extends OrderItemEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrderEntity(forProperty("order")) : null;
        this.product = inits.isInitialized("product") ? new com.ehas.lotto.product.entity.QProductEntity(forProperty("product")) : null;
    }

}

