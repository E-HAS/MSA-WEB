package com.xekidd.stomp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xekidd.stomp.Entity.StomeRoomMessageEntity;
import com.xekidd.stomp.Entity.StomeRoomMessageEntityPK;

public interface RoomMessageJpaRepository extends JpaRepository<StomeRoomMessageEntity,StomeRoomMessageEntityPK>{

}
