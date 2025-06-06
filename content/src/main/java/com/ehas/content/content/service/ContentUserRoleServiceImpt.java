package com.ehas.content.content.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.common.dto.ResponseDto;
import com.ehas.content.content.dto.ContentUserDto;
import com.ehas.content.content.dto.ContentUserRoleDto;
import com.ehas.content.content.entity.ContentUserRoleEntity;
import com.ehas.content.content.entity.QContentUserEntity;
import com.ehas.content.content.entity.QContentUserRoleEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentUserRoleServiceImpt {
	private final JPAQueryFactory queryFactory;

	@Transactional
	public Page<ContentUserRoleDto> findAll(ContentUserRoleDto contentUserRoleDto, Pageable pageable){
		List<ContentUserRoleDto> ContentUserRoles = queryFactory
								            .select(Projections.fields(ContentUserRoleDto.class
								            		,QContentUserRoleEntity.contentUserRoleEntity.userSeq
								            		,QContentUserRoleEntity.contentUserRoleEntity.contentSeq
								            		,QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq
								                ))
								             .from(QContentUserRoleEntity.contentUserRoleEntity)
								             .where(getDefaultWheres(contentUserRoleDto))
								             .offset(pageable.getOffset())
								             .limit(pageable.getPageSize())
								             .orderBy(QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq.asc())
								             .fetch();
		
		long total = queryFactory.select(QContentUserRoleEntity.contentUserRoleEntity.count())
								 .from(QContentUserRoleEntity.contentUserRoleEntity)
								 .fetchOne();
		
		return new PageImpl<>(ContentUserRoles, pageable, total);
	}
	
	@Transactional
	public ContentUserRoleEntity findContentUserRole(ContentUserRoleDto contentUserRoleDto){
		return queryFactory
	            .select(Projections.fields(ContentUserRoleEntity.class
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
	public Boolean addListContentUserRole(List<ContentUserRoleDto> contentUserRoleDtoList){
		JPAInsertClause insert = queryFactory.insert(QContentUserRoleEntity.contentUserRoleEntity)
												.columns(QContentUserRoleEntity.contentUserRoleEntity.userSeq
														,QContentUserRoleEntity.contentUserRoleEntity.contentSeq
														,QContentUserRoleEntity.contentUserRoleEntity.contentRoleSeq);
		for(ContentUserRoleDto dto : contentUserRoleDtoList) {
			insert.values(dto.getUserSeq()
						, dto.getContentSeq()
						, dto.getContentRoleSeq());
		}
				
		 return insert.execute() > 0 ? true : false;
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
