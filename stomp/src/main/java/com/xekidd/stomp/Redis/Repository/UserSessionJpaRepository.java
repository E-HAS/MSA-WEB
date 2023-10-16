package com.xekidd.stomp.Redis.Repository;

import org.springframework.data.repository.CrudRepository;

import com.xekidd.stomp.Redis.Entity.UserSession;

public interface UserSessionJpaRepository extends CrudRepository<UserSession, String> {
}
