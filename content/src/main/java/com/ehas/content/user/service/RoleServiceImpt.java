package com.ehas.content.user.service;

import com.ehas.content.user.dto.RoleDto;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.repository.RoleRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpt {
	private final RoleRepository RoleRepository;
	
	public RoleEntity findRole(Integer seq){
		return RoleRepository.findById(seq).get();
	}
	
	public List<RoleEntity> findAllRole(){
		return RoleRepository.findAll();
	}
	
	public Boolean addRole(RoleDto roleDto){
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
	
	public Boolean ModifyRole(RoleDto roleDto){
		try {
			RoleRepository.updateByseq(roleDto.getSeq()
					,roleDto.getRoleName()
					,roleDto.getRoleDept());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	
	public Boolean deleteRole(Integer seq){
		try {
			RoleRepository.deleteById(seq);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	
	public RoleEntity findRoleByUserSeq(Integer userSeq){
		return RoleRepository.findByUserSeq(userSeq);
	}
	
	public RoleEntity findRoleByUserId(String userId){
		return RoleRepository.findByUserId(userId);
	}
}
