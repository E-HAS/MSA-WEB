package com.ehas.content.user.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleEntityKey implements Serializable{

	private Integer userSeq;
	private Integer roleSeq;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleEntityKey)) return false;
        UserRoleEntityKey userroleEntity = (UserRoleEntityKey) o;
        return Objects.equals(userSeq, userroleEntity.userSeq) && Objects.equals(roleSeq, userroleEntity.roleSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash( userSeq, roleSeq);
    }
}
