package com.ehas.auth.User.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ehas.auth.User.userstatus.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="USER")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements UserDetails{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    //@Column(nullable = false, length = 100)
    private String id;

    //@Column(nullable = false, length = 60)
    private String password;

    //@Column(nullable = false, length = 100)
    private String name;

    //@Convert(converter = UserStatusConverter.class)
    //@Column(name = "status", nullable = false)
    private UserStatus status;
    
    private Integer addressSeq;

    //@Column(name = "password_updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime passwordUpdatedDate;

    //@Column(name = "registered_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime registeredDate;

    //@Column(name = "updated_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime updatedDate;

    //@Column(name = "deleted_date", columnDefinition = "DATETIME(3)")
    private LocalDateTime deletedDate;
    
    @Transient
    private List<RoleEntity> roles = new ArrayList<RoleEntity>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<String> lists = new ArrayList<String>();
    	this.roles.forEach(v -> { lists.add(v.getRoleName());});
    	
        return  lists
        		.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.id;
    }
    
	@Override
	public String getPassword() {
		return this.password;
	}

	// 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        if(this.status == UserStatus.ACCOUNT_EXPIRED){
            return false;
        }
        return true;
    }

    // 잠긴 계정
    @Override
    public boolean isAccountNonLocked() {
        if(this.status == UserStatus.ACCOUNT_LOCKED){
            return false;
        }
        return true;
    }

    // 패스워드 만료
    @Override
    public boolean isCredentialsNonExpired() {
        if(this.status == UserStatus.CREDENTIALS_EXPIRED){
            return false;
        }
        return true;
    }
    @Override
    public boolean isEnabled() {
        if(isCredentialsNonExpired() && isAccountNonExpired() && isAccountNonLocked()){
            return true;
        }
        return false;
    }

}