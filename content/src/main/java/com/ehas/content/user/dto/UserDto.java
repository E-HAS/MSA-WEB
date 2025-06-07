package com.ehas.content.user.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
	private Integer seq;
	private String id;
	private String name;
	private String password;
	private Integer status;
	
	private Integer addressSeq;
	private Integer roleSeq;
	
	@Transient
	@Builder.Default
	private List<String> roles = new ArrayList<String>();
	
	public interface UserData {
	    String getSeq();
	    String getName();
	    String getAddressSeq();
	}
}
