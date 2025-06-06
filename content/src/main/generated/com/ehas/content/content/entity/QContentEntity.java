package com.ehas.content.content.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentEntity is a Querydsl query type for ContentEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentEntity extends EntityPathBase<ContentEntity> {

    private static final long serialVersionUID = 921164935L;

    public static final QContentEntity contentEntity = new QContentEntity("contentEntity");

    public final com.ehas.content.content.base.QDateEntityBase _super = new com.ehas.content.content.base.QDateEntityBase(this);

    public final StringPath contentDept = createString("contentDept");

    public final StringPath contentName = createString("contentName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> registeredDate = _super.registeredDate;

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public final BooleanPath used = createBoolean("used");

    public final ListPath<ContentUserEntity, QContentUserEntity> users = this.<ContentUserEntity, QContentUserEntity>createList("users", ContentUserEntity.class, QContentUserEntity.class, PathInits.DIRECT2);

    public QContentEntity(String variable) {
        super(ContentEntity.class, forVariable(variable));
    }

    public QContentEntity(Path<? extends ContentEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentEntity(PathMetadata metadata) {
        super(ContentEntity.class, metadata);
    }

}

