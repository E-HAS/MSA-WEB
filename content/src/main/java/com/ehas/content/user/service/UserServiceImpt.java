package com.ehas.content.user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.entity.QRoleEntity;
import com.ehas.content.user.entity.QUserEntity;
import com.ehas.content.user.entity.QUserRoleEntity;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.repository.RoleRepository;
import com.ehas.content.user.repository.UserRepository;
import com.ehas.content.user.repository.UserRoleRepository;
import com.ehas.content.user.userstatus.UserStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final UserRepository UserRepository;
	private final RoleRepository RoleRepository;
	private final UserRoleRepository UserRoleRepository;
	
	private final JPAQueryFactory queryFactory;
	//@Transactional(rollbackFor = { Exception.class })  
	public Boolean saveByUser(UserDto user){
		try {
			UserEntity userEntity = UserRepository.save(UserEntity.builder()
									.id(user.getId())
									.password(user.getPassword())
									.name(user.getName())
									.addressSeq(user.getAddressSeq())
									.status(UserStatus.ACTIVE)
									.registeredDate(LocalDateTime.now())
									.build());
			
			UserRoleEntity roleEntity = UserRoleRepository.save(UserRoleEntity
															.builder()
															.userSeq(userEntity.getSeq())
															.roleSeq(user.getRoleSeq())
															.build());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	//@Transactional(rollbackFor = { Exception.class })  
	public boolean updateByUser(UserDto userDto){
		try {
			UserEntity findUserEntity = this.findByUserId(userDto.getId());
					
			if(!isStringNullOrEmpty(userDto.getName())) {
				findUserEntity.setName(userDto.getName());
			}
			if(!isStringNullOrEmpty(userDto.getPassword())) {
				findUserEntity.setPassword(userDto.getPassword());
				findUserEntity.setPasswordUpdatedDate(LocalDateTime.now());
			}
			if(userDto.getStatus() != null) {
				findUserEntity.setStatus(UserStatus.getString(userDto.getStatus()));
			}
			if( userDto.getAddressSeq() != null ) {
				findUserEntity.setAddressSeq(userDto.getAddressSeq());
			}
				findUserEntity.setUpdatedDate(LocalDateTime.now());
			return this.updateByUserBySeq(findUserEntity)?true:false;		
		}catch(Exception e) {
			return false;
		}
	}
	
	//@Transactional(rollbackFor = { Exception.class })  
	public boolean deleteByUser(String userId){
		try {
			UserEntity findUserEntity = this.findByUserId(userId);
			findUserEntity.setStatus(UserStatus.INACTIVE);
			findUserEntity.setDeletedDate(LocalDateTime.now());
			return this.updateByUserBySeq(findUserEntity) ? true : false;
		}catch(Exception e) {
			return false;
		}
	}
	
	
	public UserEntity findByUserSeq(Integer seq){
		return UserRepository.findBySeq(seq);
	}
	
	public UserEntity findByUserId(String id){
		QUserEntity qUserEntity = QUserEntity.userEntity;
		QUserRoleEntity qUserRoleEntity = QUserRoleEntity.userRoleEntity;
		QRoleEntity qRoleEntity = QRoleEntity.roleEntity;
		
		List<UserEntity> result = queryFactory
							.select(qUserEntity)
							.from(qUserEntity)
							.join(qUserEntity.roles, qUserRoleEntity)
							.join(qUserRoleEntity.role,qRoleEntity)
							.where(qUserEntity.id.eq(id))
							.fetch();

		result.forEach(v->{
			v.getRoles().forEach(e->{
				log.info(e.getUser().toString());
				log.info(e.getRole().toString());
				log.info(e.toString());
			});
		});
		
		return UserRepository.findById(id);
	}
	
	public RoleEntity findRoleByUserSeq(Integer seq){
		return RoleRepository.findByUserSeq(seq);
	}
	
	public Boolean updateByUserBySeq(UserEntity entity){
		return UserRepository.updateUserBySeq(entity.getSeq()
												,entity.getName()
												,entity.getPassword()
												,entity.getStatus()
												,entity.getAddressSeq()
												,entity.getPasswordUpdatedDate()
												,entity.getUpdatedDate()
												,entity.getDeletedDate()) == 1 ? true : false;
	}
	
	public boolean isStringNullOrEmpty(String str) {
	    return str == null || str.trim().isEmpty();
	}
}
