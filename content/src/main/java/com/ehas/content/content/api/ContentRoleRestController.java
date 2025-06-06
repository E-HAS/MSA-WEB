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
import com.ehas.content.content.dto.ContentRoleDto;
import com.ehas.content.content.entity.ContentRoleEntity;
import com.ehas.content.content.service.ContentRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentRoleRestController {
	private final ContentRoleServiceImpt contentRoleServiceImpt;
	
	@GetMapping(path="/{contentSeq}/roles")
	public ResponseEntity<ResponseDto> getContentRoleAll(@PathVariable ("contentSeq") Integer contentSeq
														,@PageableDefault(size = 15) Pageable pageable){
		try {
			Page<ContentRoleDto> lists = contentRoleServiceImpt.findAll(ContentRoleDto.builder()
																						.contentSeq(contentSeq)
																						.build(), pageable);
		
			return  ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDto.builder()
					.status(HttpStatus.OK.value())
					.message(HttpStatus.OK.getReasonPhrase())
					.data(Map.of("role", lists))
					.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(ResponseDto.builder()
							.status(HttpStatus.BAD_REQUEST.value())
							.message(e.getMessage())
							.build());
		}
	}
	
	@GetMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public ResponseEntity<ResponseDto> getContentRole(@PathVariable ("contentSeq") Integer contentSeq
																,@PathVariable ("contentRoleSeq") Integer contentRoleSeq){
		try {
			ContentRoleEntity findEntity = contentRoleServiceImpt.findContentRole(ContentRoleDto.builder()
																								.seq(contentRoleSeq)
																								.contentSeq(contentSeq)
																								.build());
			return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
														.body(ResponseDto.builder()
														.status(HttpStatus.OK.value())
														.message(HttpStatus.OK.getReasonPhrase())
														.data(Map.of("role", findEntity))
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
	
	@PostMapping(path="/{contentSeq}/roles")
	public ResponseEntity<ResponseDto> addContentRole(@RequestBody ContentRoleDto contentRoleDto){
		try {
			Boolean result = contentRoleServiceImpt.addContentRole(contentRoleDto);
			
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
	
	@PutMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public ResponseEntity<ResponseDto> updateContent(@PathVariable ("contentSeq") Integer contentSeq
														,@PathVariable ("contentRoleSeq") Integer contentRoleSeq
														,@RequestBody ContentRoleDto contentRoleDto){
		try {
			contentRoleDto.setSeq(contentRoleSeq);
			contentRoleDto.setContentSeq(contentSeq);
			Boolean result = contentRoleServiceImpt.updateContentRole(contentRoleDto);
			
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
	
	@DeleteMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public ResponseEntity<ResponseDto> deleteContent(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentRoleSeq") Integer contentRoleSeq){
		try {
			Boolean result = contentRoleServiceImpt.deleteContentRole(ContentRoleDto.builder()
																					.seq(contentRoleSeq)
																					.contentSeq(contentSeq)
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
