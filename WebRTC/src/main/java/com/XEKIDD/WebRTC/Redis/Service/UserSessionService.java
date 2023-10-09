package com.XEKIDD.WebRTC.Redis.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.XEKIDD.WebRTC.Redis.Entity.UserSession;
import com.XEKIDD.WebRTC.Redis.Repository.UserSessionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

	private final UserSessionRepository repository;
	
	@Transactional
	public UserSession addUser(UserSession _uSession) {
		UserSession  uSession = repository.save(_uSession);
		return uSession;
	}
}
