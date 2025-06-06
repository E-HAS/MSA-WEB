package com.ehas.content.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.common.user.status.UserStatus;
import com.ehas.content.common.utill.validation;
import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.dto.UserRoleDto;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.principal.entity.UserDetail;
import com.ehas.content.user.redis.dto.RedisUserDto;
import com.ehas.content.user.redis.service.UserRedisSerivceImpt;
import com.ehas.content.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final UserRepository userRepository;
	private final UserRoleServiceImpt userRoleServiceImpt;
	private final UserRedisSerivceImpt userRedisSerivceImpt;
	
	@Transactional(rollbackFor = { Exception.class })  
	public Boolean add(UserDto user) throws Exception{
		try {
			UserEntity userEntity = userRepository.save(UserEntity.builder()
									.id(user.getId())
									.password(user.getPassword())
									.name(user.getName())
									.addressSeq(user.getAddressSeq())
									.status(UserStatus.ACTIVE)
									.registeredDate(LocalDateTime.now())
									.build());
			
			UserRoleEntity roleEntity = userRoleServiceImpt.add(UserRoleDto.builder()
																				.userSeq(userEntity.getSeq())
																				.roleSeq(user.getRoleSeq())
																				.build());
			return true;
		}catch(Exception e) {
			log.error("User Registration Failed Error : "+e.getMessage());
			throw new Exception("User Registration Failed");
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean updateBySeq(UserEntity entity){
		return userRepository.updateBySeq(entity.getSeq()
												,entity.getName()
												,entity.getPassword()
												,entity.getStatus()
												,entity.getAddressSeq()
												,entity.getPasswordUpdatedDate()
												,entity.getUpdatedDate()
												,entity.getDeletedDate()) == 1 ? true : false;
	}
	
	@Transactional(rollbackFor = { Exception.class })  
	public Boolean updateBySeq(UserDto userDto){
		try {
			UserEntity findUserEntity = this.findByUserId(userDto.getId());
					
			if(!validation.isStringNullOrEmpty(userDto.getName())) {
				findUserEntity.setName(userDto.getName());
			}
			if(!validation.isStringNullOrEmpty(userDto.getPassword())) {
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
			
			return this.updateBySeq(findUserEntity)? true:false;		
		}catch(Exception e) {
			log.error("User Update Failed Error : "+e.getMessage());
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })  
	public Boolean delete(String userId){
		try {
			UserEntity findUserEntity = this.findByUserId(userId);
			//UserRepository.delete(findUserEntity);
			
			findUserEntity.setStatus(UserStatus.INACTIVE);
			findUserEntity.setDeletedDate(LocalDateTime.now());
			return this.updateBySeq(findUserEntity) ? true : false;
		}catch(Exception e) {
			log.error("User Delete Failed Error : "+e.getMessage());
			return false;
		}
	}
	
	public UserEntity findByUserSeq(Integer seq){
		return userRepository.findByUserSeq(seq);
	}
	
	@Transactional
	public UserEntity findByUserId(String id){
		UserEntity entity = userRepository.findByUserId(id);
		List<String> roles = new ArrayList<String>();
		entity.getRoles().forEach(v -> { 
			roles.add(v.getRole().getRoleName());
    	});
		
		if(!userRedisSerivceImpt.exists(id)) {
			userRedisSerivceImpt.save(id, RedisUserDto.builder()
	                .userSeq(entity.getSeq())
	                .addressSeq(entity.getAddressSeq())
	                .name(entity.getName())
	                .Status(entity.getStatus().getInteger())
	                .roles(roles)
	                .build());
		}
		
		return entity;
	}

	public Page<UserDto> findByAll(Integer seq,
									Integer status,
									String id,
									String name,
									Pageable pageable){
		
		return userRepository.findDtoByStatus(status, pageable);
	}
}
