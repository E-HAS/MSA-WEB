package com.ehas.auth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ehas.auth.reactive.ReactiveUserRepository;

import lombok.RequiredArgsConstructor;

/*
@RequiredArgsConstructor
@Service
@Primary
public class CustomUserDetailService implements UserDetailsService {

    private final ReactiveUserRepository userRepositoryRx;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryRx.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}*/