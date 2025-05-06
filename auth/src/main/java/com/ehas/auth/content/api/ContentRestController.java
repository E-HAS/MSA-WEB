package com.ehas.auth.content.api;


import java.util.Map;

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

import com.ehas.auth.User.dto.ResponseDto;
import com.ehas.auth.content.dto.ContentDto;
import com.ehas.auth.content.service.ContentServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentRestController {
	private final ContentServiceImpt contentServiceImpt;
	
	@GetMapping
	public Mono<ResponseEntity<ResponseDto>> getContentAll(){
		return contentServiceImpt.findAll()
				.collectList()
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
																	.body(ResponseDto.builder()
																	.status(HttpStatus.OK.value())
																	.message(HttpStatus.OK.getReasonPhrase())
																	.data(Map.of("cotnent", findEntity))
																	.build())
																: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																					.body(ResponseDto.builder()
																					.status(HttpStatus.BAD_REQUEST.value())
																					.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																					.build());
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	@GetMapping(path="/{contentSeq}")
	public Mono<ResponseEntity<ResponseDto>> getContent(@PathVariable ("contentSeq") Integer contentSeq){
		return contentServiceImpt.findBySeq(contentSeq)
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
																	.body(ResponseDto.builder()
																	.status(HttpStatus.OK.value())
																	.message(HttpStatus.OK.getReasonPhrase())
																	.data(Map.of("cotnent", findEntity))
																	.build())
																: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																					.body(ResponseDto.builder()
																					.status(HttpStatus.BAD_REQUEST.value())
																					.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																					.build());
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	
	@PostMapping
	public Mono<ResponseEntity<ResponseDto>> addContent(@RequestBody ContentDto contentDto){
		return contentServiceImpt.addContent(contentDto)
				.map(result ->{ return result ? ResponseEntity.status(HttpStatus.CREATED)
																.body(ResponseDto.builder()
																.status(HttpStatus.CREATED.value())
																.message(HttpStatus.CREATED.getReasonPhrase())
																.build())
												: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																			.body(ResponseDto.builder()
																			.status(HttpStatus.BAD_REQUEST.value())
																			.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																			.build());
								})
								.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(ResponseDto.builder()
								.status(HttpStatus.BAD_REQUEST.value())
								.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
								.build()));
	}
	
	@PutMapping(path="/{contentSeq}")
	public Mono<ResponseEntity<ResponseDto>> updateContent(@PathVariable ("contentSeq") Integer contentSeq
														,@RequestBody ContentDto contentDto){
		return contentServiceImpt.updateContent(contentDto)
				.map(result ->{ return result ? ResponseEntity.status(HttpStatus.OK)
																.body(ResponseDto.builder()
																.status(HttpStatus.OK.value())
																.message(HttpStatus.OK.getReasonPhrase())
																.build())
												: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																			.body(ResponseDto.builder()
																			.status(HttpStatus.BAD_REQUEST.value())
																			.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																			.build());
								})
								.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(ResponseDto.builder()
								.status(HttpStatus.BAD_REQUEST.value())
								.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
								.build()));
	}
	
	@DeleteMapping(path="/{contentSeq}")
	public Mono<ResponseEntity<ResponseDto>> deleteContent(@PathVariable ("contentSeq") Integer contentSeq){
		return contentServiceImpt.deleteContent(contentSeq)
				.map(result ->{ return result ? ResponseEntity.status(HttpStatus.NO_CONTENT)
																.body(ResponseDto.builder()
																.status(HttpStatus.NO_CONTENT.value())
																.message(HttpStatus.NO_CONTENT.getReasonPhrase())
																.build())
												: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																	.body(ResponseDto.builder()
																	.status(HttpStatus.BAD_REQUEST.value())
																	.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																	.build());
								})
								.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(ResponseDto.builder()
								.status(HttpStatus.BAD_REQUEST.value())
								.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
								.build()));
	}
}
