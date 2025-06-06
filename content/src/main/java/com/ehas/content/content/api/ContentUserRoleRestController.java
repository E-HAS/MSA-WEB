package com.ehas.content.content.api;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehas.content.common.dto.ResponseDto;
import com.ehas.content.content.dto.ContentUserRoleDto;
import com.ehas.content.content.entity.ContentUserRoleEntity;
import com.ehas.content.content.service.ContentUserRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentUserRoleRestController {
	private final ContentUserRoleServiceImpt contentUsersRoleServiceImpt;
	
	@GetMapping(path="/{contentSeq}/users/{contentUserSeq}/roles")
	public ResponseEntity<ResponseDto> getContentUserRole(@PathVariable ("contentSeq") Integer contentSeq
													,@PathVariable ("contentUserSeq") Integer contentUserSeq
													,@PageableDefault(size = 15) Pageable pageable){
		try {
			Page<ContentUserRoleDto> lists = contentUsersRoleServiceImpt.findAll(ContentUserRoleDto.builder()
																						.contentSeq(contentSeq)
																						.userSeq(contentUserSeq)
																						.build()
																		 , pageable);
			
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
	
	@GetMapping(path="/{contentSeq}/users/{contentUserSeq}/roles/{roleSeq}")
	public ResponseEntity<ResponseDto> getContentUserRoles(@PathVariable ("contentSeq") Integer contentSeq
													 ,@PathVariable ("contentUserSeq") Integer contentUserSeq
													 ,@PathVariable ("roleSeq") Integer roleSeq){
		try {
			ContentUserRoleEntity findEntity =  contentUsersRoleServiceImpt.findContentUserRole(ContentUserRoleDto.builder()
																									.contentSeq(contentSeq)
																									.userSeq(contentUserSeq)
																									.contentRoleSeq(roleSeq)
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
	
	@PostMapping(path="/{contentSeq}/users/{contentUserSeq}/roles")
	public ResponseEntity<ResponseDto> addContentUserRole(	@PathVariable ("contentSeq") Integer contentSeq
			 											,@PathVariable ("contentUserSeq") Integer contentUserSeq
														,@RequestBody ContentUserRoleDto contentUserRoleDto){
		try {
			contentUserRoleDto.setContentSeq(contentSeq);
			Boolean result = contentUsersRoleServiceImpt.addContentUserRole(contentUserRoleDto);
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
	
	@PutMapping(path="/{contentSeq}/users/{contentUserSeq}/roles")
	@Transactional
	public ResponseEntity<ResponseDto> updateContentUserRole(@PathVariable ("contentSeq") Integer contentSeq
														,@PathVariable ("contentUserSeq") Integer contentUserSeq
														,@RequestBody List<ContentUserRoleDto> contentUserRoleDtos){
		try {
			Boolean deleted = contentUsersRoleServiceImpt.deleteContentUserRole(ContentUserRoleDto.builder()
																									.contentSeq(contentSeq)
																									.userSeq(contentUserSeq)
																									.build());
			if(!deleted) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body(ResponseDto.builder()
										.status(HttpStatus.BAD_REQUEST.value())
										.message(HttpStatus.BAD_REQUEST.getReasonPhrase())
										.build());
			}
			
			contentUserRoleDtos.forEach(v->{
				v.setContentSeq(contentSeq);
				v.setUserSeq(contentUserSeq);
			});
			
			Boolean result = contentUsersRoleServiceImpt.addListContentUserRole(contentUserRoleDtos);
			
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
	
	@DeleteMapping(path="/{contentSeq}/users//{contentUserSeq}/roles/{roleSeq}")
	public ResponseEntity<ResponseDto> deleteContentRole(@PathVariable ("contentSeq") Integer contentSeq
															,@PathVariable ("contentUserSeq") Integer contentUserSeq
															,@PathVariable ("roleSeq") Integer roleSeq){
		try {
			Boolean result = contentUsersRoleServiceImpt.deleteContentUserRole(ContentUserRoleDto.builder()
																					.contentSeq(contentSeq)
																					.userSeq(contentUserSeq)
																					.contentRoleSeq(roleSeq)
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
