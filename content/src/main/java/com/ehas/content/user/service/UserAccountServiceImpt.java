package com.ehas.content.user.service;
import com.ehas.content.user.dto.UserAccountDto;
import com.ehas.content.user.entity.UserAccountEntity;
import com.ehas.content.user.repository.UserAccountJpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpt {
	private final UserAccountJpaRepository userAccountJpaRepository;
	
	@Transactional(rollbackFor = { Exception.class })
	public UserAccountEntity add(UserAccountDto dto){
		try {
			 return userAccountJpaRepository.save(UserAccountEntity.builder()
																.seq(dto.getSeq())
																.seq(dto.getUserSeq())
																.balance(dto.getBalance())
																.status(dto.getStatus())
																.created_date(LocalDateTime.now())
																.build());
		}catch(Exception e) {
			return null;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean UpdateByUserSeq(UserAccountDto dto){
		try {
			userAccountJpaRepository.updateByUserSeq(dto.getUserSeq()
														,dto.getBalance()
														,dto.getStatus());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean updateBalanceForPaymentByUserSeq(UserAccountDto dto){
		try {
			userAccountJpaRepository.updateBalanceForPaymentByUserSeq(dto.getUserSeq()
																,dto.getBalance());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean updateBalanceForChargeByUserSeq(UserAccountDto dto){
		try {
			userAccountJpaRepository.updateBalanceForChargeByUserSeq(dto.getUserSeq()
																,dto.getBalance());
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public Boolean delete(Integer seq){
		try {
			userAccountJpaRepository.deleteById(seq);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	public UserAccountEntity find(Integer seq){
		return userAccountJpaRepository.findById(seq).orElse(null);
	}
	
	public UserAccountEntity findByUserSeq(Integer userSeq){
		return userAccountJpaRepository.findByUserSeq(userSeq);
	}
	
	public List<UserAccountEntity> findAll(){
		return userAccountJpaRepository.findAll();
	}

}
