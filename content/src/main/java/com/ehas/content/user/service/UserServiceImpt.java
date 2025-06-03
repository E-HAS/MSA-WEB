package com.ehas.content.user.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.user.dto.UserDetail;
import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.dto.UserRoleDto;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.repository.UserRepository;
import com.ehas.content.user.userstatus.UserStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpt {

	private final UserRepository UserRepository;
	private final UserRoleServiceImpt UserRoleServiceImpt;
	
	@Transactional(rollbackFor = { Exception.class })  
	public Boolean add(UserDto user){
		try {
			UserEntity userEntity = UserRepository.save(UserEntity.builder()
									.id(user.getId())
									.password(user.getPassword())
									.name(user.getName())
									.addressSeq(user.getAddressSeq())
									.status(UserStatus.ACTIVE)
									.registeredDate(LocalDateTime.now())
									.build());
			
			UserRoleEntity roleEntity = UserRoleServiceImpt.add(UserRoleDto.builder()
																				.userSeq(userEntity.getSeq())
																				.roleSeq(user.getRoleSeq())
																				.build());
			return true;
		}catch(Exception e) {
			new Exception("Insert Failed User");
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean updateBySeq(UserEntity entity){
		return UserRepository.updateBySeq(entity.getSeq()
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
			
			return this.updateBySeq(findUserEntity)? true:false;		
		}catch(Exception e) {
			new Exception("Update Failed User");
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
			new Exception("Delete Failed User");
			return false;
		}
	}
	
	public UserEntity findByUserSeq(Integer seq){
		return UserRepository.findByUserSeq(seq);
	}
	
	@Transactional
	public UserEntity findByUserId(String id){
		return UserRepository.findByUserId(id);
	}
	
	@Transactional
	public UserDetail findUserDetailByUserId(String id){
		UserEntity entity = UserRepository.findByUserId(id);
		List<String> roles = new ArrayList<String>();
		entity.getRoles().forEach(v -> { 
			roles.add(v.getRole().getRoleName());
    	});
		
		UserDetail userDetail = new UserDetail(entity);
		userDetail.setRoles(roles);
		
		return userDetail;
	}
	
	public Boolean isStringNullOrEmpty(String str) {
	    return str == null || str.trim().isEmpty();
	}
}
