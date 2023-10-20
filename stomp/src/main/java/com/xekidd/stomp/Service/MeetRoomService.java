package com.xekidd.stomp.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xekidd.stomp.Redis.Entity.MeetRoom;
import com.xekidd.stomp.Redis.Entity.MeetRoomUser;
import com.xekidd.stomp.Redis.Repository.MeetRoomJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetRoomService {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final MeetRoomJpaRepository meetRoomJpaRepository;
	private final PasswordEncoder passwordEncoder;
	
	public MeetRoom saveRoom(MeetRoom _mRoom) {
		MeetRoom mRoom = meetRoomJpaRepository.save(_mRoom);
		return mRoom;
	}
	public MeetRoom addRoom(MeetRoom _mRoom) {
		
		// password encode
		String encPassword = passwordEncoder.encode(_mRoom.getRoomPassword());
		_mRoom.setRoomPassword(encPassword);
		
		// add Room
		MeetRoom mRoom = meetRoomJpaRepository.save(_mRoom);
		return mRoom;
	}
	public MeetRoom findRoomByRoomId(String _roomId) {
		// find room by id
		Optional<MeetRoom> mRoom = meetRoomJpaRepository.findById(_roomId);
		if(!mRoom.isPresent()) {
			return null;
		}
		return mRoom.get();
	}
	public boolean removeRoom(MeetRoom _mRoom) {
		// delete room
		meetRoomJpaRepository.delete(_mRoom);
		return true;
	}
	public boolean removeRoomByRoomId(String _roomId) {
		// delete room by id
		meetRoomJpaRepository.deleteById(_roomId);
		return true;
	}
	public boolean removeUserInRoomByRoomIdAndUserName(String _roomId,String _userName) {
		MeetRoom mRoom = findRoomByRoomId(_roomId);
		
		MeetRoomUser result = mRoom.getUserLists().remove(_userName);
		if(result == null) {
			return false;
		}
		
		return true;
	}
	
	public boolean equalsRoomPassword(String _roomId, String _roomPassword) {
		// find room by id
		MeetRoom mRoom = findRoomByRoomId(_roomId);
		
		// check room
		if(findRoomByRoomId(_roomId) == null) {
			return false;
		}
		
		// check password
		String mRoomPassword = mRoom.getRoomPassword();
		logger.info("mRoomPassword {} param {} ",mRoomPassword, _roomPassword);
		 if (!passwordEncoder.matches(_roomPassword, mRoomPassword)) {
			 return false;
	     }
		
		return true;
	}
	
	public List<MeetRoomUser> findUserListInRoomByRoomId(String _roomId) {
		List<MeetRoomUser> _mRoomUserList = findRoomByRoomId(_roomId)
														.getUserLists()
														.values()
														.stream()
														.collect(Collectors.toList());
		return _mRoomUserList;
	}
	
	public List<String> findUserNameListInRoomByRoomId(String _roomId) {
		List<String> _mRoomUserNameList = findRoomByRoomId(_roomId)
														.getUserLists()
														.keySet()
														.stream()
														.collect(Collectors.toList());
		return _mRoomUserNameList;
	}
	
	public MeetRoomUser findUserInRoomByRoomId(String _roomId, String _userName) {
		MeetRoomUser _mRoomUser = findRoomByRoomId(_roomId)
														.getUserLists()
														.entrySet()
														.stream()
														.filter(key->key.getKey().equals(_userName))
														.findFirst()
														.get()
														.getValue();
		
		return _mRoomUser;
	}
}
