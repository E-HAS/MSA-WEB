package com.ehas.auth.content.entity;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentRoleEntityKey implements Serializable{

	private Integer contentSeq;
	private Integer roleSeq;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentRoleEntityKey)) return false;
        ContentRoleEntityKey that = (ContentRoleEntityKey) o;
        return Objects.equals(contentSeq, that.contentSeq) &&  Objects.equals(roleSeq, that.roleSeq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentSeq,  roleSeq);
    }
}
