package com.XEKIDD.WebRTC.Redis.Repository;

import org.springframework.data.repository.CrudRepository;

import com.XEKIDD.WebRTC.Redis.Entity.UserSession;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {
}
