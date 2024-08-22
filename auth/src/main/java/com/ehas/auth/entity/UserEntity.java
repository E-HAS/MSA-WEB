package com.ehas.auth.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="User")
public class UserEntity implements UserDetails{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(value="user_type")
    private Integer userType;
    @Column(value="user_id")
    private String userId;
    @Column(value="nick_name")
    private String nickName;
    @Column(value="user_password")
    private String userPassword;
    
    @Column(value="user_state")
    private String userState; // Y : 정상 회원 , L : 잠긴 계정, P : 패스워드 만료, A : 계정 만료
    
    @Column(value="registered_date")
    @CreatedDate
    private LocalDateTime registeredDate;
    
    @Column(value="updated_date")
    @LastModifiedDate
    private LocalDateTime updatedDate;
    
    @Column(value="deleted_date")
    private LocalDateTime deletedDate;
    @Column(value="password_updated_date")
    private LocalDateTime passwordUpdatedDate;
    
    @Transient
    private List<UserRole> roles = new ArrayList<UserRole>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	System.out.println(">>>> getAuthorities1");
    	List<String> lists = new ArrayList<String>();
    	this.roles.forEach(v -> { lists.add(v.getUserRole());});
    	
    	//return List.of(new SimpleGantedAuthority(role.name()));
    	
        return  lists
        		.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.userId;
    }
    
	@Override
	public String getPassword() {
		return this.userPassword;
	}

	// 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        if(StringUtils.startsWithIgnoreCase(userState, "A")){
            return false;
        }
        return true;
    }

    // 잠긴 계정
    @Override
    public boolean isAccountNonLocked() {
        if(StringUtils.startsWithIgnoreCase(userState, "L")){
            return false;
        }
        return true;
    }

    // 패스워드 만료
    @Override
    public boolean isCredentialsNonExpired() {
        if(StringUtils.startsWithIgnoreCase(userState, "P")){
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