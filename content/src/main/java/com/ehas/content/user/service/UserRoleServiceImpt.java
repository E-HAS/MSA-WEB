package com.ehas.content.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.dto.UserRoleDto;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.entity.UserRoleEntityKey;
import com.ehas.content.user.repository.RoleRepository;
import com.ehas.content.user.repository.UserRepository;
import com.ehas.content.user.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpt {
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	
	@Transactional(rollbackFor = { Exception.class })
	public UserRoleEntity add(UserRoleDto userRoleDto){
		return userRoleRepository.save(UserRoleEntity.builder()
														.userSeq(userRoleDto.getUserSeq())
														.roleSeq(userRoleDto.getRoleSeq())
														.build());
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean delete(UserRoleDto userRoleDto){
		try {
			userRoleRepository.deleteById(UserRoleEntityKey.builder()
						.userSeq(userRoleDto.getUserSeq())
						.roleSeq(userRoleDto.getRoleSeq())
						.build());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean deleteRoleByUserIdAndRoleSeq(String userId, Integer roleSeq){
		try {
			userRoleRepository.findByUserIdAndRoleSeq(userId, roleSeq);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public List<UserRoleDto> findByUserId(String userId){
		UserDto userDto = (UserDto) userRepository.getUserById(userId);
		
		return userRoleRepository.findByUserSeq(userDto.getSeq()).stream()
														.map(v-> UserRoleDto.builder()
																			.userSeq(v.getUserSeq())
																			.roleSeq(v.getRoleSeq())
																			.roleName(v.getRole().getRoleName())
																			.roleDept(v.getRole().getRoleDept())
																			.build())
														.toList();
	}
	
}
