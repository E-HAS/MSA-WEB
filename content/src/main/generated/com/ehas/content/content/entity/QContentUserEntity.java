package com.ehas.content.content.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentUserEntity is a Querydsl query type for ContentUserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentUserEntity extends EntityPathBase<ContentUserEntity> {

    private static final long serialVersionUID = 776175154L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentUserEntity contentUserEntity = new QContentUserEntity("contentUserEntity");

    public final QContentEntity content;

    public final NumberPath<Integer> contentSeq = createNumber("contentSeq", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> deletedDate = createDateTime("deletedDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> registeredDate = createDateTime("registeredDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final EnumPath<com.ehas.content.user.userstatus.UserStatus> status = createEnum("status", com.ehas.content.user.userstatus.UserStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedDate = createDateTime("updatedDate", java.time.LocalDateTime.class);

    public final StringPath userDept = createString("userDept");

    public final StringPath userName = createString("userName");

    public final NumberPath<Integer> userSeq = createNumber("userSeq", Integer.class);

    public QContentUserEntity(String variable) {
        this(ContentUserEntity.class, forVariable(variable), INITS);
    }

    public QContentUserEntity(Path<? extends ContentUserEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentUserEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentUserEntity(PathMetadata metadata, PathInits inits) {
        this(ContentUserEntity.class, metadata, inits);
    }

    public QContentUserEntity(Class<? extends ContentUserEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new QContentEntity(forProperty("content")) : null;
    }

}

