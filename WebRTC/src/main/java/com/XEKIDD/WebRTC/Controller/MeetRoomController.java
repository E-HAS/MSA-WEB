package com.XEKIDD.WebRTC.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

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
		if(pwCheck) {
			Optional<MeetRoom> mtRoom = roomRepository.findRoomByStringId(roomId);
			
			MeetRoomDto mtRoomDto = new MeetRoomDto();
			mtRoomDto.setRoomId(roomId);
			mtRoomDto.setRoomName(mtRoom.get().getRoomName());
			mtRoomDto.setRoomPassword(roomPwd);
			mtRoomDto.setUserName(userName);
			
			reAttr.addFlashAttribute("room",mtRoomDto);
			reAttr.addFlashAttribute("isName",userName);
			reAttr.addFlashAttribute("isAdmin",false);
			return "redirect:/Meet/MeetRoom";
		}else {
			//reAttr.addFlashAttribute("room",mtRoom.get());
			reAttr.addFlashAttribute("isName",userName);
			return "Meet/Enter";
		}
	}
	
	@PostMapping("/Create/{roomName}/{userName}")
	public String CreateRoom(@PathVariable String roomName, @PathVariable String userName, @RequestParam String roomPwd, RedirectAttributes reAttr) {
		logger.info("/Create//{}/{} >> RoomPassword {}",roomName, userName, roomPwd);
		//String roomId = UUID.randomUUID().toString();
		String roomId =randomId();
		MeetRoomDto mtRoom = new MeetRoomDto();
		mtRoom.setRoomId(roomId);
		mtRoom.setRoomName(roomName);
		mtRoom.setRoomPassword(roomPwd);
		mtRoom.setUserName(userName);
		
		
		roomRepository.addRoom(
								MeetRoom.builder()
								.roomId(roomId)
								.roomName(roomName)
								.roomPasswrd(roomPwd).build());
		reAttr.addFlashAttribute("room",mtRoom);
		reAttr.addFlashAttribute("isName",userName);
		reAttr.addFlashAttribute("isAdmin",true);
		
		return "redirect:/Meet/MeetRoom";
	}
	
	@GetMapping("/MeetRoom")
	public String inMeetRoom(Model model, HttpServletRequest request) {
		 model.addAttribute("Model", model);
		 Map<String, ?> flashMap =RequestContextUtils.getInputFlashMap(request);
		logger.info("/Meet/MeetRoom >> GET {}", flashMap);
		return "Meet/MeetRoom";
	}
	
	@PostMapping("/MeetRoom")
	public String inMeetRoom(Model model, @RequestBody Map<String, String> Map) {
		 model.addAttribute("Model", model);
		logger.info("/Meet/MeetRoom >> POST {}", Map);
		return "Meet/MeetRoom";
	}
	
	private String randomId() {
		 StringBuffer key = new StringBuffer();
	      Random rnd = new Random();

	      for (int i = 0; i < 6; i++) { 
	    	 int index = rnd.nextInt(3);
	           switch (index) {
	           case 0:
	               key.append((char)((int) (rnd.nextInt(26)) + 97));
	               break;
	           case 1:
	               key.append((char)((int) (rnd.nextInt(26)) + 65));
	               break;
	           case 2:
	               key.append((rnd.nextInt(10)));
	               break;
	           }
	      }
	      return key.toString();
	}
}
