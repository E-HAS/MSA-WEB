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
import com.ehas.auth.content.dto.ContentRoleDto;
import com.ehas.auth.content.service.ContentRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentRoleRestController {
	private final ContentRoleServiceImpt contentRoleServiceImpt;
	
	@GetMapping(path="/{contentSeq}/roles")
	public Mono<ResponseEntity<ResponseDto>> getContentRoleAll(@PathVariable ("contentSeq") Integer contentSeq){
		return contentRoleServiceImpt.findByContentSeq(contentSeq)
				.collectList()
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
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
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	@GetMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public Mono<ResponseEntity<ResponseDto>> getContentRoleAll(@PathVariable ("contentSeq") Integer contentSeq
																,@PathVariable ("contentRoleSeq") Integer contentRoleSeq){
		return contentRoleServiceImpt.findBySeq(contentRoleSeq)
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
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
									})
									.onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body(ResponseDto.builder()
									.status(HttpStatus.BAD_REQUEST.value())
									.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
									.build()));
	}
	
	@PostMapping(path="/{contentSeq}/roles")
	public Mono<ResponseEntity<ResponseDto>> addContentRole(@RequestBody ContentRoleDto contentRoleDto){
		return contentRoleServiceImpt.addContentRole(contentRoleDto)
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
	
	@PutMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public Mono<ResponseEntity<ResponseDto>> updateContent(@PathVariable ("contentSeq") Integer contentSeq
														,@PathVariable ("contentRoleSeq") Integer contentRoleSeq
														,@RequestBody ContentRoleDto contentRoleDto){
		return contentRoleServiceImpt.updateContentRole(contentRoleDto)
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
	
	@DeleteMapping(path="/{contentSeq}/roles/{contentRoleSeq}")
	public Mono<ResponseEntity<ResponseDto>> deleteContent(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentRoleSeq") Integer contentRoleSeq){
		return contentRoleServiceImpt.deleteContentRole(contentRoleSeq)
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
