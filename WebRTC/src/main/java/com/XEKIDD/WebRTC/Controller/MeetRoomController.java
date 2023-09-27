package com.XEKIDD.WebRTC.Controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.XEKIDD.WebRTC.Domain.MeetRoom;
import com.XEKIDD.WebRTC.Domain.MeetRoomDto;
import com.XEKIDD.WebRTC.Repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/Meet")
public class MeetRoomController {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final RoomRepository roomRepository;
	
	@GetMapping("/Enter")
	public String enterRoom(Model model, MeetRoomDto room) {
		 model.addAttribute("MeetRoomDto", room);
		logger.info("/Enter >>");
		return "Meet/Enter";
	}
	
	@PostMapping("/Enter/{roomId}/{userName}")
	public String enterRoom(@PathVariable String roomId, @PathVariable String userName, @RequestParam String roomPwd, RedirectAttributes reAttr) {
		logger.info("/Enter/{roomId}/{userName} >>");
		Boolean pwCheck = roomRepository.checkPwdInRoom(roomId, roomPwd);
		MeetRoom mtRoom = MeetRoom.builder()
							.roomId(roomId)
							.roomPasswrd(roomPwd).build();
		if(pwCheck) {
			reAttr.addFlashAttribute("room",mtRoom);
			reAttr.addFlashAttribute("isName",userName);
			reAttr.addFlashAttribute("isAdmin",true);
			return "/Meet/Room";
		}else {
			reAttr.addFlashAttribute("room",mtRoom);
			reAttr.addFlashAttribute("isName",userName);
			return "/Meet/Enter";
		}
	}
	
	@PostMapping("/Create/{roomName}/{userName}")
	public String CreateRoom(@PathVariable String roomName, @PathVariable String userName, @RequestParam String roomPwd, RedirectAttributes reAttr) {
		logger.info("/Create/{}/{} >> RoomPassword {}",roomName, userName, roomPwd);
		String roomId = UUID.randomUUID().toString();
		MeetRoom mtRoom = MeetRoom.builder()
									.roomId(roomId)
									.roomName(roomName)
									.roomPasswrd(roomPwd).build();
		
		roomRepository.addRoom(mtRoom);
		reAttr.addFlashAttribute("room",mtRoom);
		reAttr.addFlashAttribute("isName",userName);
		reAttr.addFlashAttribute("isAdmin",true);
		
		return "/Meet/Room";
	}
}
