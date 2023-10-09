package com.XEKIDD.WebRTC.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import com.XEKIDD.WebRTC.Domain.MeetRoom;
import com.XEKIDD.WebRTC.Domain.MeetUserSession;

@Repository
public class RoomRepository {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	 // global rooms
	 private Set<MeetRoom> rooms = new TreeSet<MeetRoom>(Comparator.comparing(MeetRoom::getRoomId));
	 
	 // user information
	 // session ID, room
	 private ConcurrentHashMap<String, MeetRoom> RoomMaps = new ConcurrentHashMap<>();
	 
	    // add in Global Room
	    public Boolean addRoom(final MeetRoom room) {
	        return rooms.add(room);
	    }
	    // check password in Global Room
	    public Boolean checkPwdInRoom(String roomId, String roomPwd) {
	    	 return findRoomByStringId(roomId).get().getRoomPasswrd().equals(roomPwd);
	    }
	    
	    // remove in Global Room
	    public void removeInRoom(String roomId, String sessionId) {
	    	Optional<MeetRoom> _room = findRoomByStringId(roomId);  // find in Global Room
	    	Optional<String> _key = _room.get().getClients().entrySet().stream() // user list in Global Room 
	    							.filter(value -> value.getValue().getWebSocketSession().getId().equals(sessionId)) // session ID compare value
	    							.map(Map.Entry::getKey)
	    							.findFirst();
	    	_room.get().getClients().remove(_key.get()); // remove in Global Room
	    }
	    
	    // roomId find Room
	    public Optional<MeetRoom> findRoomByStringId(final String rid) {
	    	return rooms.stream().filter(r -> r.getRoomId().equals(rid)).findAny();
		    
	    }
	    // add Client In Global Room
	    public MeetUserSession addClient(final MeetRoom room, final String fromId, final String userName, final WebSocketSession session) {
	        return room.getClients().put(fromId, MeetUserSession.builder().UserName(userName).webSocketSession(session).build());
	    }

	    // get Clients In Global Room
	    public Map<String, MeetUserSession> getClients(final MeetRoom room) {
	        return Optional.ofNullable(room)
	                .map(r -> Collections.unmodifiableMap(r.getClients())) // read-only 객체 생성
	                .orElse(Collections.emptyMap());
	    }
	    
	    // get Clients Name In Global Room
	    public List<String> getNameInClients(final MeetRoom room){
			return getClients(room).entrySet().stream().map(key -> key.getKey()).collect(Collectors.toList());
					
	    }
	    

	    
	    // add user information 
	    public void putRoomMaps(String sessionId, MeetRoom room) {
	    	RoomMaps.put(sessionId, room);
	    }
	    
	    // remove user information 
	    public MeetRoom removeRoomMaps(String sessionId) {
	    	return RoomMaps.remove(sessionId);
	    }
	    
	    // get user information 
	    public MeetRoom getRoomInRoomMaps(String sessionId) {
	    	return RoomMaps.get(sessionId);
	    }
}
