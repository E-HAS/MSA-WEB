package com.ehas.lotto.lotto.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLottoNumberStatEntity is a Querydsl query type for LottoNumberStatEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLottoNumberStatEntity extends EntityPathBase<LottoNumberStatEntity> {

    private static final long serialVersionUID = 1165912593L;

    public static final QLottoNumberStatEntity lottoNumberStatEntity = new QLottoNumberStatEntity("lottoNumberStatEntity");

    public final NumberPath<Short> five = createNumber("five", Short.class);

    public final NumberPath<Short> four = createNumber("four", Short.class);

    public final SimplePath<LottoNumberStatId> id = createSimple("id", LottoNumberStatId.class);

    public final NumberPath<Short> one = createNumber("one", Short.class);

    public final NumberPath<Float> percent = createNumber("percent", Float.class);

    public final NumberPath<Short> six = createNumber("six", Short.class);

    public final NumberPath<Integer> sum = createNumber("sum", Integer.class);

    public final NumberPath<Short> three = createNumber("three", Short.class);

    public final NumberPath<Short> two = createNumber("two", Short.class);

    public QLottoNumberStatEntity(String variable) {
        super(LottoNumberStatEntity.class, forVariable(variable));
    }

    public QLottoNumberStatEntity(Path<? extends LottoNumberStatEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLottoNumberStatEntity(PathMetadata metadata) {
        super(LottoNumberStatEntity.class, metadata);
    }

}

