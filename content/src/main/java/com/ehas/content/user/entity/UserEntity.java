package com.ehas.content.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ehas.content.common.user.status.UserStatus;
import com.ehas.content.common.user.status.UserStatus.UserStatusConverter;
import com.ehas.content.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(exclude = "roles")
@JsonIgnoreProperties({ "password", "status", "passwordUpdatedDate", "registeredDate", "updatedDate", "deletedDate", "authorities" })
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(nullable = false, length = 100)
    private String id;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Convert(converter = UserStatusConverter.class)
    @Column(name = "status", nullable = false)
    private UserStatus status;
    
    private Integer addressSeq;

    @Column(name = "password_updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime passwordUpdatedDate;

    @Column(name = "registered_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime registeredDate;

    @Column(name = "updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedDate;

    @Column(name = "deleted_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime deletedDate;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user") // mappedBy= 프로퍼티 이름
    @Builder.Default
    private List<UserRoleEntity> roles = new ArrayList<UserRoleEntity>();
    
    
    public UserDto convertUserDto() {
    	return UserDto.builder()
    				  .seq(this.seq)
    				  .id(this.id)
    				  .name(this.name)
    				  .addressSeq(this.addressSeq)
    				  .build();
    }
}