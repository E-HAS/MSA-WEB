package com.XEKIDD.WebRTC.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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
	 
	 private Set<MeetRoom> rooms = new TreeSet<MeetRoom>(Comparator.comparing(MeetRoom::getRoomId));
	 private Map<String, MeetRoom> RoomMaps = new HashMap<>();
	 
	    public Boolean addRoom(final MeetRoom room) {
	        return rooms.add(room);
	    }
	 
	    public Set<MeetRoom> getRooms() {
	        final TreeSet<MeetRoom> defensiveCopy = new TreeSet<>(Comparator.comparing(MeetRoom::getRoomId));
	        defensiveCopy.addAll(rooms);

	        return defensiveCopy;
	    }
	    public Boolean checkPwdInRoom(String roomId, String roomPwd) {
	    	 return findRoomByStringId(roomId).get().getRoomPasswrd().equals(roomPwd);
	    }
	    
	    public void removeInRoom(String roomId, String sessionId) {
	    	Optional<MeetRoom> _room = findRoomByStringId(roomId);
	    	Optional<String> _key = _room.get().getClients().entrySet().stream()
	    							.filter(value -> value.getValue().getWebSocketSession().getId().equals(sessionId))
	    							.map(Map.Entry::getKey)
	    							.findFirst();
	    	_room.get().getClients().remove(_key.get());
	    }

	    public Optional<MeetRoom> findRoomByStringId(final String sid) {
	        // simple get() because of parser errors handling
	        //return rooms.stream().filter(r -> r.getId().equals(parseId(sid).get())).findAny();
	    	return rooms.stream().filter(r -> r.getRoomId().equals(sid)).findAny();
		    
	    }

	    public String getRoomId(MeetRoom room) {
	        return room.getRoomId();
	    }

	    public Map<String, MeetUserSession> getClients(final MeetRoom room) {
	        return Optional.ofNullable(room)
	                .map(r -> Collections.unmodifiableMap(r.getClients())) // read-only 객체 생성
	                .orElse(Collections.emptyMap());
	    }

	    public MeetUserSession addClient(final MeetRoom room, final String fromId, final String userName, final WebSocketSession session) {
	        return room.getClients().put(fromId, MeetUserSession.builder().UserName(userName).webSocketSession(session).build());
	    }

	    public MeetUserSession removeClientByName(final MeetRoom room, final String name) {
	        return room.getClients().remove(name);
	    }
	    /*
	    public Optional<Long> parseId(String sid) {
	        Long id = null;
	        try {
	            id = Long.valueOf(sid);
	        } catch (Exception e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }

	        return Optional.ofNullable(id);
	    }
	    */
	    public void putRoomMaps(String sessionId, MeetRoom room) {
	    	RoomMaps.put(sessionId, room);
	    }
	    public void removeRoomMaps(String sessionId) {
	    	RoomMaps.remove(sessionId);
	    }
	    public MeetRoom getRoomInRoomMaps(String sessionId) {
	    	return RoomMaps.get(sessionId);
	    }
}
