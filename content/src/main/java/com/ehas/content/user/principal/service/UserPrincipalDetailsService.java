package com.ehas.content.user.principal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.principal.entity.UserDetail;
import com.ehas.content.user.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("UserUserDetailService")
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserServiceImpt userServiceImpt;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	UserEntity user = userServiceImpt.findByUserId(username);
    	
    	UserDetail userDetail = new UserDetail(user);
		userDetail.setRoles(user.getRoles().stream()
										 .map(v->v.getRole().getRoleName())
										 .toList());
		
		return userDetail;
    }
}