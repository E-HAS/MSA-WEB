package com.XEKIDD.WebRTC.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import com.XEKIDD.WebRTC.Domain.Room;

@Repository
public class RoomRepository {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 
	 private Set<Room> rooms = new TreeSet<Room>(Comparator.comparing(Room::getId));
	 private Map<String, Room> RoomMaps = new HashMap<>();
	 
	    public Boolean addRoom(final Room room) {
	        return rooms.add(room);
	    }
	 
	    public Set<Room> getRooms() {
	        final TreeSet<Room> defensiveCopy = new TreeSet<>(Comparator.comparing(Room::getId));
	        defensiveCopy.addAll(rooms);

	        return defensiveCopy;
	    }

	    public Optional<Room> findRoomByStringId(final String sid) {
	        // simple get() because of parser errors handling
	        return rooms.stream().filter(r -> r.getId().equals(parseId(sid).get())).findAny();
	    }

	    public String getRoomId(Room room) {
	        return room.getId();
	    }

	    public Map<String, WebSocketSession> getClients(final Room room) {
	        return Optional.ofNullable(room)
	                .map(r -> Collections.unmodifiableMap(r.getClients())) // read-only 객체 생성
	                .orElse(Collections.emptyMap());
	    }

	    public WebSocketSession addClient(final Room room, final String name, final WebSocketSession session) {
	        return room.getClients().put(name, session);
	    }

	    public WebSocketSession removeClientByName(final Room room, final String name) {
	        return room.getClients().remove(name);
	    }
	    
	    public Optional<Long> parseId(String sid) {
	        Long id = null;
	        try {
	            id = Long.valueOf(sid);
	        } catch (Exception e) {
	            logger.debug("An error occured: {}", e.getMessage());
	        }

	        return Optional.ofNullable(id);
	    }
	    
	    public void putRoomMaps(String sessionId, Room room) {
	    	RoomMaps.put(sessionId, room);
	    }
	    public void removeRoomMaps(String sessionId) {
	    	RoomMaps.remove(sessionId);
	    }
	    public Room getRoomInRoomMaps(String sessionId) {
	    	return RoomMaps.get(sessionId);
	    }
}
