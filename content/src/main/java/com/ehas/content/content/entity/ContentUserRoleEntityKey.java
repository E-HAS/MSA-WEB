package com.ehas.content.content.entity;

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
public class ContentUserRoleEntityKey implements Serializable{

	private Integer userSeq;
	private Integer contentSeq;
	private Integer contentRoleSeq;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentUserRoleEntity)) return false;
        ContentUserRoleEntity contentUserRoleEntity = (ContentUserRoleEntity) o;
        return Objects.equals(this.userSeq, contentUserRoleEntity.getUserSeq()) 
        		&& Objects.equals(this.contentSeq, contentUserRoleEntity.getContentSeq())
        		&& Objects.equals(this.contentRoleSeq, contentUserRoleEntity.getContentRoleSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash( this.userSeq, this.contentSeq, this.contentRoleSeq);
    }
}
