package com.ehas.content.content.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.content.dto.ContentUserRoleDto;
import com.ehas.content.content.entity.ContentUserRoleEntity;
import com.ehas.content.content.entity.QContentUserRoleEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentUserRoleServiceImpt {
	private final JPAQueryFactory queryFactory;

	@Transactional
	public ContentUserRoleEntity findContentUserRole(ContentUserRoleDto contentUserRoleDto){
		return queryFactory
	            .select(Projections.constructor(ContentUserRoleEntity.class
	            		,QContentUserRoleEntity.contentUserRoleEntity.userSeq
	            		,QContentUserRoleEntity.contentUserRoleEntity.contentSeq
	            		,QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq
	                ))
	             .from(QContentUserRoleEntity.contentUserRoleEntity)
	             .where(getDefaultWheres(contentUserRoleDto))
	             .fetchOne();
	}
	
	@Transactional
	public Boolean addContentUserRole(ContentUserRoleDto contentUserRoleDto){
		 return queryFactory.insert(QContentUserRoleEntity.contentUserRoleEntity)
				  .set(QContentUserRoleEntity.contentUserRoleEntity.userSeq, contentUserRoleDto.getUserSeq())
				  .set(QContentUserRoleEntity.contentUserRoleEntity.contentSeq, contentUserRoleDto.getContentSeq())
				  .set(QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq, contentUserRoleDto.getContentRoleSeq())
				  .execute() > 0 ? true : false;
	}
	
	@Transactional
	public Boolean deleteContentUserRole(ContentUserRoleDto contentUserRoleDto){
		return queryFactory.delete(QContentUserRoleEntity.contentUserRoleEntity)
				  .where(getDefaultWheres(contentUserRoleDto))
				  .execute() > 0 ? true : false;
	}
	
	private BooleanBuilder getDefaultWheres(ContentUserRoleDto contentUserRoleDto) {
	    BooleanBuilder wheres = new BooleanBuilder();
	    if (contentUserRoleDto.getUserSeq() != null) {
	    	wheres.and(QContentUserRoleEntity.contentUserRoleEntity.userSeq.eq(contentUserRoleDto.getUserSeq()));
	    }	    
	    if (contentUserRoleDto.getContentSeq() != null) {
	    	wheres.and(QContentUserRoleEntity.contentUserRoleEntity.contentSeq.eq(contentUserRoleDto.getContentSeq()));
	    }
	    if (contentUserRoleDto.getContentRoleSeq() != null) {
	    	wheres.and(QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq.eq(contentUserRoleDto.getContentRoleSeq()));
	    }
	    return wheres;
	}
}
