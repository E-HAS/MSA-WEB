package com.ehas.content.content.base;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDateEntityBase is a Querydsl query type for DateEntityBase
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QDateEntityBase extends EntityPathBase<DateEntityBase> {

    private static final long serialVersionUID = 348905481L;

    public static final QDateEntityBase dateEntityBase = new QDateEntityBase("dateEntityBase");

    public final DateTimePath<java.time.LocalDateTime> registeredDate = createDateTime("registeredDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updatedDate = createDateTime("updatedDate", java.time.LocalDateTime.class);

    public QDateEntityBase(String variable) {
        super(DateEntityBase.class, forVariable(variable));
    }

    public QDateEntityBase(Path<? extends DateEntityBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDateEntityBase(PathMetadata metadata) {
        super(DateEntityBase.class, metadata);
    }

}

