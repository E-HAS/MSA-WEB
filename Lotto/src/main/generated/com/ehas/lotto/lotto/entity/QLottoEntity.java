package com.ehas.lotto.lotto.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLottoEntity is a Querydsl query type for LottoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLottoEntity extends EntityPathBase<LottoEntity> {

    private static final long serialVersionUID = 586835892L;

    public static final QLottoEntity lottoEntity = new QLottoEntity("lottoEntity");

    public final NumberPath<Short> bonus = createNumber("bonus", Short.class);

    public final NumberPath<Short> five = createNumber("five", Short.class);

    public final NumberPath<Short> four = createNumber("four", Short.class);

    public final NumberPath<Short> one = createNumber("one", Short.class);

    public final NumberPath<Integer> round = createNumber("round", Integer.class);

    public final NumberPath<Short> six = createNumber("six", Short.class);

    public final NumberPath<Short> three = createNumber("three", Short.class);

    public final NumberPath<Short> two = createNumber("two", Short.class);

    public QLottoEntity(String variable) {
        super(LottoEntity.class, forVariable(variable));
    }

    public QLottoEntity(Path<? extends LottoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLottoEntity(PathMetadata metadata) {
        super(LottoEntity.class, metadata);
    }

}

