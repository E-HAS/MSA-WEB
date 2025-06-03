package com.ehas.content.user.service;

import com.ehas.content.user.dto.RoleDto;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpt {
	private final RoleRepository RoleRepository;
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean add(RoleDto roleDto){
		try {
				RoleRepository.save(RoleEntity.builder()
						.roleName(roleDto.getRoleName())
						.roleDept(roleDto.getRoleDept())
						.build());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean UpdateBySeq(RoleDto roleDto){
		try {
			RoleRepository.updateByseq(roleDto.getSeq()
					,roleDto.getRoleName()
					,roleDto.getRoleDept());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean delete(Integer seq){
		try {
			RoleRepository.deleteById(seq);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	public RoleEntity findById(Integer seq){
		return RoleRepository.findById(seq).orElse(null);
	}
	
	public List<RoleEntity> findAll(){
		return RoleRepository.findAll();
	}
	
	// USER 관련
	public RoleEntity findRoleByUserSeq(Integer userSeq){
		return RoleRepository.findByUserSeq(userSeq);
	}
	
	public RoleEntity findRoleByUserId(String userId){
		return RoleRepository.findByUserId(userId);
	}
}
