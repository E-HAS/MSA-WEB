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
import com.ehas.auth.content.dto.ContentRoleDto;
import com.ehas.auth.content.dto.ContentUserDto;
import com.ehas.auth.content.service.ContentUserServiceImpt;

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
	public Mono<ResponseEntity<ResponseDto>> getContentRole(@PathVariable ("contentSeq") Integer contentSeq){
		return contentUsersServiceImpt.findByContentSeq(contentSeq)
				.collectList()
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
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
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	@GetMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public Mono<ResponseEntity<ResponseDto>> getContentRole(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentUserSeq") Integer contentUserSeq){
		return contentUsersServiceImpt.findByContentSeqAndUserSeq(contentSeq, contentUserSeq)
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
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
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	@PostMapping(path="/{contentSeq}/users")
	public Mono<ResponseEntity<ResponseDto>> addContentRole(@RequestBody ContentUserDto contentUserDto){
		return contentUsersServiceImpt.addContentUser(contentUserDto)
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
	
	@PutMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public Mono<ResponseEntity<ResponseDto>> updateContent(@PathVariable ("contentSeq") Integer contentSeq
														,@PathVariable ("contentUserSeq") Integer contentUserSeq
														,@RequestBody ContentUserDto contentUserDto){
		return contentUsersServiceImpt.updateContentUser(contentUserDto)
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
	
	@DeleteMapping(path="/{contentSeq}/users/{contentUserSeq}")
	public Mono<ResponseEntity<ResponseDto>> deleteContent(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentUserSeq") Integer contentUserSeq){
		return contentUsersServiceImpt.deleteBySeq(contentUserSeq)
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
