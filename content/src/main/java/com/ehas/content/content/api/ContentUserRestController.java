package com.ehas.content.content.api;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.content.common.dto.ResponseDto;
import com.ehas.content.content.dto.ContentUserDto;
import com.ehas.content.content.entity.ContentUserEntity;
import com.ehas.content.content.service.ContentUserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentUserRestController {
	private final ContentUserServiceImpt contentUsersServiceImpt;
	
	@GetMapping(path="/{contentSeq}/users")
	public ResponseEntity<ResponseDto> getContentUsers(@PathVariable ("contentSeq") Integer contentSeq
													,@PageableDefault(size = 15) Pageable pageable){
		try {
			Page<ContentUserDto> lists = contentUsersServiceImpt.findAll(ContentUserDto.builder()
																						.contentSeq(contentSeq).build()
																		 , pageable);
			
			return  ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDto.builder()
					.status(HttpStatus.OK.value())
					.message(HttpStatus.OK.getReasonPhrase())
					.data(Map.of("user", lists))
					.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ResponseDto.builder()
					.status(HttpStatus.BAD_REQUEST.value())
					.message(e.getMessage())
					.build());
		}
	}
	
	@GetMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public ResponseEntity<ResponseDto> getContentUser(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentUserSeq") Integer contentUserSeq){
		try {
			ContentUserEntity findEntity =  contentUsersServiceImpt.findContentUser(ContentUserDto.builder()
																									.contentSeq(contentSeq)
																									.userSeq(contentUserSeq)
																									.build());
			return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDto.builder()
					.status(HttpStatus.OK.value())
					.message(HttpStatus.OK.getReasonPhrase())
					.data(Map.of("user", findEntity))
					.build())
				: ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.message(e.getMessage())
			.build());
		}
	}
	
	@PostMapping(path="/{contentSeq}/users")
	public ResponseEntity<ResponseDto> addContentUser(	@PathVariable ("contentSeq") Integer contentSeq
														,@RequestBody ContentUserDto contentUserDto){
		try {
			contentUserDto.setContentSeq(contentSeq);
			Boolean result = contentUsersServiceImpt.addContentUser(contentUserDto);
			return result ? ResponseEntity.status(HttpStatus.CREATED)
											.body(ResponseDto.builder()
											.status(HttpStatus.CREATED.value())
											.message(HttpStatus.CREATED.getReasonPhrase())
											.build())
							: ResponseEntity.status(HttpStatus.BAD_REQUEST)
														.body(ResponseDto.builder()
														.status(HttpStatus.BAD_REQUEST.value())
														.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
														.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.message(e.getMessage())
			.build());
		}
	}
	
	@PutMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public ResponseEntity<ResponseDto> updateContentUser(@PathVariable ("contentSeq") Integer contentSeq
														,@PathVariable ("contentUserSeq") Integer contentUserSeq
														,@RequestBody ContentUserDto contentUserDto){
		try {
			contentUserDto.setContentSeq(contentSeq);
			contentUserDto.setUserSeq(contentUserSeq);
			
			Boolean result = contentUsersServiceImpt.updateContentUser(contentUserDto);
			
			 return result ? ResponseEntity.status(HttpStatus.OK)
											.body(ResponseDto.builder()
											.status(HttpStatus.OK.value())
											.message(HttpStatus.OK.getReasonPhrase())
											.build())
							: ResponseEntity.status(HttpStatus.BAD_REQUEST)
														.body(ResponseDto.builder()
														.status(HttpStatus.BAD_REQUEST.value())
														.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
														.build());
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.message(e.getMessage())
			.build());
		}
	}
	
	@DeleteMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public ResponseEntity<ResponseDto> deleteContent(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentUserSeq") Integer contentUserSeq){
		try {
			Boolean result = contentUsersServiceImpt.deleteContentUser(ContentUserDto.builder()
																					.contentSeq(contentSeq)
																					.userSeq(contentUserSeq)
																					.build());
			 return result ? ResponseEntity.status(HttpStatus.NO_CONTENT)
											.body(ResponseDto.builder()
											.status(HttpStatus.NO_CONTENT.value())
											.message(HttpStatus.NO_CONTENT.getReasonPhrase())
											.build())
							: ResponseEntity.status(HttpStatus.BAD_REQUEST)
												.body(ResponseDto.builder()
												.status(HttpStatus.BAD_REQUEST.value())
												.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
												.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ResponseDto.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.message(e.getMessage())
			.build());
		}
	}
}
