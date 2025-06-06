package com.ehas.content.content.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.content.dto.ContentDto;
import com.ehas.content.content.entity.ContentEntity;
import com.ehas.content.content.entity.QContentEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentServiceImpt {
	private final JPAQueryFactory queryFactory;
	
	@Transactional
	public Page<ContentDto> findAll(ContentDto contentDto, Pageable pageable){
		List<ContentDto> contents = queryFactory
							            .select(Projections.fields(ContentDto.class,
							            		QContentEntity.contentEntity.seq
							            		,QContentEntity.contentEntity.contentName
							            		,QContentEntity.contentEntity.contentDept
							                ))
							             .from(QContentEntity.contentEntity)
							             .where(getDefaultWheres(contentDto))
							             .offset(pageable.getOffset())
							             .limit(pageable.getPageSize())
							             .orderBy(QContentEntity.contentEntity.seq.asc())
							             .fetch();
        long total = queryFactory
                .select(QContentEntity.contentEntity.count())
                .from(QContentEntity.contentEntity)
                .fetchOne();
        
        return new PageImpl<>(contents, pageable, total);
	}
	
	@Transactional
	public ContentEntity findContent(ContentDto contentDto){
		return queryFactory
	            .select(Projections.fields(ContentEntity.class,
	            		QContentEntity.contentEntity.seq
	            		,QContentEntity.contentEntity.contentName
	            		,QContentEntity.contentEntity.contentDept
	                ))
	             .from(QContentEntity.contentEntity)
	             .where(getDefaultWheres(contentDto))
	             .fetchOne();
	}
	@Transactional
	public Boolean addContent(ContentDto contentDto){
		 return queryFactory.insert(QContentEntity.contentEntity)
								  .set(QContentEntity.contentEntity.contentName, contentDto.getContentName())
								  .set(QContentEntity.contentEntity.contentDept, contentDto.getContentDept())
								  .set(QContentEntity.contentEntity.used, contentDto.getUsed())
								  .execute() > 0 ? true : false;
	}
	
	@Transactional
	public Boolean updateContent(ContentDto contentDto){
		 return queryFactory.update(QContentEntity.contentEntity)
				 				  .where(getDefaultWheres(contentDto))
								  .set(QContentEntity.contentEntity.contentName, contentDto.getContentName())
								  .set(QContentEntity.contentEntity.contentDept, contentDto.getContentDept())
								  .set(QContentEntity.contentEntity.used, contentDto.getUsed())
								  .execute() > 0 ? true : false;
	}
	
	@Transactional
	public Boolean deleteContent(ContentDto contentDto){
		 return queryFactory.delete(QContentEntity.contentEntity)
				 .					where(getDefaultWheres(contentDto))
				 					.execute() > 0 ? true : false;
	}
	
	private BooleanBuilder getDefaultWheres(ContentDto contentDto) {
	    BooleanBuilder wheres = new BooleanBuilder();
	    if (contentDto.getSeq() != null) {
	    	wheres.and(QContentEntity.contentEntity.seq.eq(contentDto.getSeq()));
	    }
	    if (contentDto.getContentName() != null) {
	    	wheres.and(QContentEntity.contentEntity.contentName.eq(contentDto.getContentName()));
	    }	    
	    if (contentDto.getUsed() != null) {
	    	wheres.and(QContentEntity.contentEntity.used.eq(contentDto.getUsed()));
	    }
	    return wheres;
	}
}
