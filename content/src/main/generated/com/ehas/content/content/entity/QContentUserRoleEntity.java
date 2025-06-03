package com.ehas.content.content.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentUserRoleEntity is a Querydsl query type for ContentUserRoleEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentUserRoleEntity extends EntityPathBase<ContentUserRoleEntity> {

    private static final long serialVersionUID = -539395384L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentUserRoleEntity contentUserRoleEntity = new QContentUserRoleEntity("contentUserRoleEntity");

    public final QContentRoleEntity contentRole;

    public final NumberPath<Integer> contentRoleSeq = createNumber("contentRoleSeq", Integer.class);

    public final NumberPath<Integer> contentSeq = createNumber("contentSeq", Integer.class);

    public final QContentUserEntity contentUser;

    public final NumberPath<Integer> userSeq = createNumber("userSeq", Integer.class);

    public QContentUserRoleEntity(String variable) {
        this(ContentUserRoleEntity.class, forVariable(variable), INITS);
    }

    public QContentUserRoleEntity(Path<? extends ContentUserRoleEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentUserRoleEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentUserRoleEntity(PathMetadata metadata, PathInits inits) {
        this(ContentUserRoleEntity.class, metadata, inits);
    }

    public QContentUserRoleEntity(Class<? extends ContentUserRoleEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentRole = inits.isInitialized("contentRole") ? new QContentRoleEntity(forProperty("contentRole")) : null;
        this.contentUser = inits.isInitialized("contentUser") ? new QContentUserEntity(forProperty("contentUser"), inits.get("contentUser")) : null;
    }

}

