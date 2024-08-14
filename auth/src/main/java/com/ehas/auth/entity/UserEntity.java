package com.ehas.auth.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="User")
@JsonIgnoreProperties({"uid","userType","userId","userName","userPassword","roles"})
public class UserEntity implements UserDetails{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(name="user_type",length = 30, nullable = false)
    private Integer userType;
    @Column(name="user_id",length = 30, nullable = false, unique = true)
    private String userId;
    @Column(name="nick_name",length = 30, nullable = false, unique = true)
    private String nickName;
    @Column(name="user_password",length = 60, nullable = false)
    private String userPassword;
    
    @Transient
    private List<UserRole> roles = new ArrayList<UserRole>();
    @Transient
    private String state; // Y : 정상 회원 , L : 잠긴 계정, P : 패스워드 만료, A : 계정 만료
    
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
        if(StringUtils.startsWithIgnoreCase(state, "A")){
            return false;
        }
        return true;
    }

    // 잠긴 계정
    @Override
    public boolean isAccountNonLocked() {
        if(StringUtils.startsWithIgnoreCase(state, "L")){
            return false;
        }
        return true;
    }

    // 패스워드 만료
    @Override
    public boolean isCredentialsNonExpired() {
        if(StringUtils.startsWithIgnoreCase(state, "P")){
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