package com.xekidd.stomp.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
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

import com.xekidd.stomp.Redis.Entity.MeetRoom;
import com.xekidd.stomp.Redis.Entity.MeetRoomUser;
import com.xekidd.stomp.Service.MeetRoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/Meet")
public class MeetRoomController {
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final MeetRoomService meetRoomService;
	
	@GetMapping("/Index")
	public String Index(Model model) {
		 model.addAttribute("user", "user");
		logger.info("/Enter >>");
		return "Index";
	}
	
	@GetMapping("/Enter")
	public String enterRoom(Model model) {
		 model.addAttribute("user", "user");
		logger.info("/Enter >>");
		return "Enter";
	}
	
	@PostMapping("/Enter/{roomId}/{userName}")
	public String enterRoom(@PathVariable String roomId, @PathVariable String userName, @RequestParam String roomPwd, RedirectAttributes reAttr
			,@RequestParam String cameraId
			,@RequestParam String mikeId
			,@RequestParam String speackerId) {
		logger.info("/Enter/{}/{} >>",roomId, userName);
		logger.info("cameraId: {}, mikeId: {}, speackerId: {} >>",cameraId, mikeId,speackerId);
		Boolean pwCheck = meetRoomService.equalsRoomPassword(roomId, roomPwd);
		if(pwCheck) {
			MeetRoom mRoom = meetRoomService.findRoomByRoomId(roomId);
			
			LocalDateTime currentDate = LocalDateTime.now(); 
			
			MeetRoomUser mRoomUser = new MeetRoomUser();
			mRoomUser.setActive(true);
			mRoomUser.setAdmin(false);
			mRoomUser.setRegDate(currentDate);
			mRoom.getUserLists().put(userName, mRoomUser);
			meetRoomService.saveRoom(mRoom);
			logger.info("/Enter/{}/{} >> mRoomUser {}",roomId, userName, mRoomUser);
			
			reAttr.addFlashAttribute("isUserName",userName);
			reAttr.addFlashAttribute("isRoomName",mRoom.getRoomName());
			reAttr.addFlashAttribute("isRoomId",roomId);
			reAttr.addFlashAttribute("isAdmin",false);
			reAttr.addFlashAttribute("isActive",true);
			
			reAttr.addFlashAttribute("isCameraId",cameraId);
			reAttr.addFlashAttribute("isMikeId",mikeId);
			reAttr.addFlashAttribute("isSpeackerId",speackerId);
			
			return "redirect:/Meet/MeetRoom";
		}else {
			//reAttr.addFlashAttribute("room",mtRoom.get());
			reAttr.addFlashAttribute("isName",userName);
			return "Enter";
		}
	}
	
	@PostMapping("/Create/{roomName}/{userName}")
	public String CreateRoom(@PathVariable String roomName, @PathVariable String userName, @RequestParam String roomPwd, RedirectAttributes reAttr
			,@RequestParam String cameraId
			,@RequestParam String mikeId
			,@RequestParam String speackerId) {
		logger.info("/Create/{}/{} >> RoomPassword {}",roomName, userName, roomPwd);
		logger.info("cameraId: {}, mikeId: {}, speackerId: {} >>",cameraId, mikeId,speackerId);
		//String roomId = UUID.randomUUID().toString();
		String roomId =randomId();
		LocalDateTime currentDate = LocalDateTime.now(); 
		
		MeetRoom mRoom = new MeetRoom();
		mRoom.setRoomId(roomId);
		mRoom.setRoomName(roomName);
		mRoom.setRoomPassword(roomPwd);
		mRoom.setRegDate(currentDate);
		
		MeetRoomUser mRoomUser = new MeetRoomUser();
		mRoomUser.setActive(true);
		mRoomUser.setAdmin(true);
		mRoomUser.setRegDate(currentDate);
		mRoom.setUserLists(new HashMap<String, MeetRoomUser>());
		mRoom.getUserLists().put(userName, mRoomUser);
		
		MeetRoom maddRoom = meetRoomService.addRoom(mRoom);
		logger.info("/Create/{}/{} >> maddRoom {}",roomName, userName, maddRoom);
		reAttr.addFlashAttribute("isUserName",userName);
		reAttr.addFlashAttribute("isRoomName",mRoom.getRoomName());
		reAttr.addFlashAttribute("isRoomId",roomId);
		reAttr.addFlashAttribute("isAdmin",true);
		reAttr.addFlashAttribute("isActive",true);
		
		reAttr.addFlashAttribute("isCameraId",cameraId);
		reAttr.addFlashAttribute("isMikeId",mikeId);
		reAttr.addFlashAttribute("isSpeackerId",speackerId);
		
		return "redirect:/Meet/MeetRoom";
	}
	
	@GetMapping("/MeetRoom")
	public String inMeetRoom(Model model, HttpServletRequest request) {
		 model.addAttribute("Model", "Model");
		 Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		logger.info("/Meet/MeetRoom >> GET {}", flashMap);
		return "MeetRoom";
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
