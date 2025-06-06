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
import com.ehas.content.content.dto.ContentDto;
import com.ehas.content.content.entity.ContentEntity;
import com.ehas.content.content.service.ContentServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentRestController {
	private final ContentServiceImpt contentServiceImpt;
	
	@GetMapping
	public ResponseEntity<ResponseDto> getContentAll(@PageableDefault(size = 15) Pageable pageable){
		try {
			Page<ContentDto> lists = contentServiceImpt.findAll(ContentDto.builder().build(), pageable);
			
			return  ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDto.builder()
					.status(HttpStatus.OK.value())
					.message(HttpStatus.OK.getReasonPhrase())
					.data(Map.of("cotnent", lists))
					.build());
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ResponseDto.builder()
					.status(HttpStatus.BAD_REQUEST.value())
					.message(e.getMessage())
					.build());
		}
	}
	
	@GetMapping(path="/{contentSeq}")
	public ResponseEntity<ResponseDto> getContent(@PathVariable ("contentSeq") Integer contentSeq){
		try {
			ContentEntity findEntity = contentServiceImpt.findContent(ContentDto.builder().seq(contentSeq).build());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseDto.builder()
					.status(HttpStatus.OK.value())
					.message(HttpStatus.OK.getReasonPhrase())
					.data(Map.of("cotnent", findEntity))
					.build());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ResponseDto.builder()
					.status(HttpStatus.BAD_REQUEST.value())
					.message(e.getMessage())
					.build());
		}
	}
	
	
	@PostMapping
	public ResponseEntity<ResponseDto> addContent(@RequestBody ContentDto contentDto){
		try {
			Boolean result = contentServiceImpt.addContent(contentDto);
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
	
	@PutMapping(path="/{contentSeq}")
	public ResponseEntity<ResponseDto> updateContent(@PathVariable ("contentSeq") Integer contentSeq
														,@RequestBody ContentDto contentDto){
		try {
			Boolean result = contentServiceImpt.updateContent(contentDto);
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
	
	@DeleteMapping(path="/{contentSeq}")
	public ResponseEntity<ResponseDto> deleteContent(@PathVariable ("contentSeq") Integer contentSeq){
		try {
			Boolean result = contentServiceImpt.deleteContent(ContentDto.builder().seq(contentSeq).build());
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
}
