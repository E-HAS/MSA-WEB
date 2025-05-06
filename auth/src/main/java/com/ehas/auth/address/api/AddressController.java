package com.ehas.auth.address.api;


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
import com.ehas.auth.address.service.AddressServiceImpt;
import com.ehas.auth.content.dto.ContentDto;
import com.ehas.auth.content.service.ContentServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
	private final AddressServiceImpt addressServiceImpt;
	@GetMapping
	public Mono<ResponseEntity<ResponseDto>> getAddressAll(){
		return addressServiceImpt.findAll()
				.collectList()
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
																	.body(ResponseDto.builder()
																	.status(HttpStatus.OK.value())
																	.message(HttpStatus.OK.getReasonPhrase())
																	.data(Map.of("address", findEntity))
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
	
	@GetMapping(path="/{addressSeq}")
	public Mono<ResponseEntity<ResponseDto>> getAddress(@PathVariable ("addressSeq") Integer addressSeq){
		return addressServiceImpt.findBySeq(addressSeq)
				.map(findEntity ->{ return findEntity != null ? ResponseEntity.status(HttpStatus.OK)
																	.body(ResponseDto.builder()
																	.status(HttpStatus.OK.value())
																	.message(HttpStatus.OK.getReasonPhrase())
																	.data(Map.of("address", findEntity))
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
