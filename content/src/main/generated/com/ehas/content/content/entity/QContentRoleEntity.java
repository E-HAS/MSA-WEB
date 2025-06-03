package com.ehas.content.content.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentRoleEntity is a Querydsl query type for ContentRoleEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentRoleEntity extends EntityPathBase<ContentRoleEntity> {

    private static final long serialVersionUID = 667723421L;

    public static final QContentRoleEntity contentRoleEntity = new QContentRoleEntity("contentRoleEntity");

    public final NumberPath<Integer> contentSeq = createNumber("contentSeq", Integer.class);

    public final StringPath roleDept = createString("roleDept");

    public final StringPath roleName = createString("roleName");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public QContentRoleEntity(String variable) {
        super(ContentRoleEntity.class, forVariable(variable));
    }

    public QContentRoleEntity(Path<? extends ContentRoleEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentRoleEntity(PathMetadata metadata) {
        super(ContentRoleEntity.class, metadata);
    }

}

