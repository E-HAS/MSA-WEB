package com.xekidd.stomp.Redis.Repository;

import org.springframework.data.repository.CrudRepository;

import com.xekidd.stomp.Redis.Entity.MeetRoom;


public interface MeetRoomJpaRepository extends CrudRepository<MeetRoom, String> {

}
