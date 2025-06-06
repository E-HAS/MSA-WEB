package com.ehas.content.content.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.content.dto.ContentRoleDto;
import com.ehas.content.content.entity.ContentRoleEntity;
import com.ehas.content.content.entity.QContentRoleEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentRoleServiceImpt {
	private final JPAQueryFactory queryFactory;

	@Transactional
	public Page<ContentRoleDto> findAll(ContentRoleDto contentRoleDto, Pageable pageable){
		List<ContentRoleDto> ContentRoles = queryFactory
								            .select(Projections.fields(ContentRoleDto.class
								            		,QContentRoleEntity.contentRoleEntity.seq
								            		,QContentRoleEntity.contentRoleEntity.contentSeq
													,QContentRoleEntity.contentRoleEntity.roleName
													,QContentRoleEntity.contentRoleEntity.roleDept
								                ))
								             .from(QContentRoleEntity.contentRoleEntity)
								             .where(getDefaultWheres(contentRoleDto))
								             .offset(pageable.getOffset())
								             .limit(pageable.getPageSize())
								             .orderBy(QContentRoleEntity.contentRoleEntity.seq.asc())
								             .fetch();
		
		long total = queryFactory.select(QContentRoleEntity.contentRoleEntity.count())
								 .from(QContentRoleEntity.contentRoleEntity)
								 .fetchOne();
		
		return new PageImpl<>(ContentRoles, pageable, total);
	}
	
	public ContentRoleEntity findContentRole(ContentRoleDto contentRoleDto){
		return queryFactory
				.select(Projections.fields(ContentRoleEntity.class
						,QContentRoleEntity.contentRoleEntity.seq
						,QContentRoleEntity.contentRoleEntity.contentSeq
						,QContentRoleEntity.contentRoleEntity.roleName
						,QContentRoleEntity.contentRoleEntity.roleDept))
				.from(QContentRoleEntity.contentRoleEntity)
				.where(getDefaultWheres(contentRoleDto))
	            .fetchOne();	
	}
	
	public Boolean addContentRole(ContentRoleDto contentRoleDto){
		return queryFactory.insert(QContentRoleEntity.contentRoleEntity)
							.set(QContentRoleEntity.contentRoleEntity.contentSeq, contentRoleDto.getContentSeq())
							.set(QContentRoleEntity.contentRoleEntity.roleName, contentRoleDto.getRoleName())
							.set(QContentRoleEntity.contentRoleEntity.roleDept, contentRoleDto.getRoleDept())
							.execute() > 0 ? true : false;
	}
	
	public Boolean updateContentRole(ContentRoleDto contentRoleDto){
		return queryFactory.update(QContentRoleEntity.contentRoleEntity)
				.where(getDefaultWheres(contentRoleDto))
				.set(QContentRoleEntity.contentRoleEntity.contentSeq, contentRoleDto.getContentSeq())
				.set(QContentRoleEntity.contentRoleEntity.roleName, contentRoleDto.getRoleName())
				.set(QContentRoleEntity.contentRoleEntity.roleDept, contentRoleDto.getRoleDept())
				.execute() > 0 ? true : false;
	}
	
	public Boolean deleteContentRole(ContentRoleDto contentRoleDto){
		return queryFactory.delete(QContentRoleEntity.contentRoleEntity)
				.where(getDefaultWheres(contentRoleDto))
				.execute() > 0 ? true : false;
	}

	private BooleanBuilder getDefaultWheres(ContentRoleDto contentRoleDto) {
		BooleanBuilder wheres = new BooleanBuilder();
		if (contentRoleDto.getSeq() != null) {
	    	wheres.and(QContentRoleEntity.contentRoleEntity.seq.eq(contentRoleDto.getSeq()));
	    }
		if (contentRoleDto.getRoleName() != null) {
	    	wheres.and(QContentRoleEntity.contentRoleEntity.roleName.eq(contentRoleDto.getRoleName()));
	    }
		
		return wheres;
	}
}