package com.ehas.auth.User.api;

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
import com.ehas.auth.User.dto.RoleDto;
import com.ehas.auth.User.service.RoleServiceImpt;
import com.ehas.auth.User.service.UserRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleRestController {
	private final RoleServiceImpt roleServiceImpt;
	
	@GetMapping
	public Mono<ResponseEntity<ResponseDto>> getRoles() {
	    return roleServiceImpt.findAllRole()
	            .collectList()
	            .flatMap(findEntity -> {
	            	return !findEntity.isEmpty() ?  Mono.just(ResponseEntity.status(HttpStatus.OK)
									                            .body(ResponseDto.builder()
									                                    .status(HttpStatus.OK.value())
									                                    .message(HttpStatus.OK.getReasonPhrase())
									                                    .data(Map.of("Role", findEntity))
									                                    .build()))
								              	:  Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
								                            .body(ResponseDto.builder()
								                                    .status(HttpStatus.BAD_REQUEST.value())
								                                    .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
								                                    .build()));
	             }).log()
	            .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(ResponseDto.builder()
	                            .status(HttpStatus.BAD_REQUEST.value())
	                            .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
	                            .build()));
	}
	
	@PostMapping
	public Mono<ResponseEntity<ResponseDto>> addRole(@RequestBody RoleDto roleDto){
		return roleServiceImpt.addRole(roleDto)
				.map(addEntity ->{ return addEntity != null ? ResponseEntity.status(HttpStatus.CREATED)
																			.body(ResponseDto.builder()
																			.status(HttpStatus.CREATED.value())
																			.message(HttpStatus.CREATED.getReasonPhrase())
																			.build())
															: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																						.body(ResponseDto.builder()
																						.status(HttpStatus.BAD_REQUEST.value())
																						.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																						.build());
			}).onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					 .body(ResponseDto.builder()
					 .status(HttpStatus.BAD_REQUEST.value())
					 .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
					 .build()));
		
	}
	
	@PutMapping
	public Mono<ResponseEntity<ResponseDto>> modifyRole(@RequestBody RoleDto roleDto){
		return roleServiceImpt.ModifyRole(roleDto)
				.map(addEntity ->{ return addEntity != null ? ResponseEntity.status(HttpStatus.OK)
																			.body(ResponseDto.builder()
																			.status(HttpStatus.OK.value())
																			.message(HttpStatus.OK.getReasonPhrase())
																			.build())
															: ResponseEntity.status(HttpStatus.BAD_REQUEST)
																						.body(ResponseDto.builder()
																						.status(HttpStatus.BAD_REQUEST.value())
																						.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
																						.build());
			}).onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					 .body(ResponseDto.builder()
					 .status(HttpStatus.BAD_REQUEST.value())
					 .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
					 .build()));
		
	}
	
	@DeleteMapping(path="/{seq}")
	public Mono<ResponseEntity<ResponseDto>> deleteRole(@PathVariable ("seq") Integer seq){
		return roleServiceImpt.deleteRole(seq)
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
