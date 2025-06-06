package com.ehas.content.content.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.common.user.status.UserStatus;
import com.ehas.content.content.dto.ContentUserDto;
import com.ehas.content.content.entity.ContentUserEntity;
import com.ehas.content.content.entity.QContentUserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentUserServiceImpt {
	private final JPAQueryFactory queryFactory;

	@Transactional
	public Page<ContentUserDto> findAll(ContentUserDto contentUserDto, Pageable pageable){
		List<ContentUserDto> ContentUsers = queryFactory
								            .select(Projections.fields(ContentUserDto.class
								            		,QContentUserEntity.contentUserEntity.seq
								            		,QContentUserEntity.contentUserEntity.contentSeq
								            		,QContentUserEntity.contentUserEntity.userSeq
								            		,QContentUserEntity.contentUserEntity.userName
								            		,QContentUserEntity.contentUserEntity.userDept
								                ))
								             .from(QContentUserEntity.contentUserEntity)
								             .where(getDefaultWheres(contentUserDto))
								             .offset(pageable.getOffset())
								             .limit(pageable.getPageSize())
								             .orderBy(QContentUserEntity.contentUserEntity.seq.asc())
								             .fetch();
		long total = queryFactory.select(QContentUserEntity.contentUserEntity.count())
								 .from(QContentUserEntity.contentUserEntity)
								 .fetchOne();
		return new PageImpl<>(ContentUsers, pageable, total);
	}
	
	@Transactional
	public ContentUserEntity findContentUser(ContentUserDto contentUserDto){
		return queryFactory
	            .select(Projections.fields(ContentUserEntity.class
	            		,QContentUserEntity.contentUserEntity.seq
	            		,QContentUserEntity.contentUserEntity.contentSeq
	            		,QContentUserEntity.contentUserEntity.userSeq
	            		,QContentUserEntity.contentUserEntity.userName
	            		,QContentUserEntity.contentUserEntity.userDept
	                ))
	             .from(QContentUserEntity.contentUserEntity)
	             .where(getDefaultWheres(contentUserDto))
	             .fetchOne();
	}
	
	@Transactional
	public Boolean addContentUser(ContentUserDto contentUserDto){
		 return queryFactory.insert(QContentUserEntity.contentUserEntity)
				  .set(QContentUserEntity.contentUserEntity.contentSeq, contentUserDto.getContentSeq())
				  .set(QContentUserEntity.contentUserEntity.userSeq, contentUserDto.getUserSeq())
				  .set(QContentUserEntity.contentUserEntity.userName, contentUserDto.getUserName())
				  .set(QContentUserEntity.contentUserEntity.userDept, contentUserDto.getUserDept())
				  .set(QContentUserEntity.contentUserEntity.status, UserStatus.INACTIVE)
				  .execute() > 0 ? true : false;
	}
	
	@Transactional
	public Boolean updateContentUser(ContentUserDto contentUserDto){
		 return queryFactory.update(QContentUserEntity.contentUserEntity)
				  .where(getDefaultWheres(contentUserDto))
				  .set(QContentUserEntity.contentUserEntity.contentSeq, contentUserDto.getContentSeq())
				  .set(QContentUserEntity.contentUserEntity.userSeq, contentUserDto.getUserSeq())
				  .set(QContentUserEntity.contentUserEntity.userName, contentUserDto.getUserName())
				  .set(QContentUserEntity.contentUserEntity.userDept, contentUserDto.getUserDept())
				  .set(QContentUserEntity.contentUserEntity.status, UserStatus.INACTIVE)
				  .execute() > 0 ? true : false;
	}
	
	@Transactional
	public Boolean deleteContentUser(ContentUserDto contentUserDto){
		return queryFactory.delete(QContentUserEntity.contentUserEntity)
				  .where(getDefaultWheres(contentUserDto))
				  .execute() > 0 ? true : false;
	}
	
	private BooleanBuilder getDefaultWheres(ContentUserDto contentUserDto) {
	    BooleanBuilder wheres = new BooleanBuilder();
	    if (contentUserDto.getSeq() != null) {
	    	wheres.and(QContentUserEntity.contentUserEntity.seq.eq(contentUserDto.getSeq()));
	    }
	    if (contentUserDto.getUserSeq() != null) {
	    	wheres.and(QContentUserEntity.contentUserEntity.userSeq.eq(contentUserDto.getUserSeq()));
	    }	    
	    if (contentUserDto.getUserName() != null) {
	    	wheres.and(QContentUserEntity.contentUserEntity.userName.eq(contentUserDto.getUserName()));
	    }
	    return wheres;
	}
}
