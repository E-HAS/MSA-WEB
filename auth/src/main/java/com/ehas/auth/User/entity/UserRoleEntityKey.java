package com.ehas.auth.User.entity;

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

	private Integer contentSeq;
	private Integer userSeq;
	private Integer roleSeq;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleEntityKey)) return false;
        UserRoleEntityKey that = (UserRoleEntityKey) o;
        return Objects.equals(contentSeq, that.contentSeq) && Objects.equals(userSeq, that.userSeq) && Objects.equals(roleSeq, that.roleSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentSeq, userSeq, roleSeq);
    }
}
