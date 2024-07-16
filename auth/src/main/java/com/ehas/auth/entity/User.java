package com.ehas.auth.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


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
public class User implements UserDetails{
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uid;

    @Column(length = 30, nullable = false)
    private Integer userType;
    @Column(length = 30, nullable = false, unique = true)
    private String userId;
    @Column(length = 30, nullable = false, unique = true)
    private String nickName;
    @Column(length = 60, nullable = false)
    private String userPassword;
    
    @Transient
    private List<UserRole> roles = new ArrayList<UserRole>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<String> lists = new ArrayList<String>();
    	this.roles.forEach(v -> { lists.add(v.getUserRole());});
    	
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}