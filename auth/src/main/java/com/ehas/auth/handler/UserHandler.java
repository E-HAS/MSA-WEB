package com.ehas.auth.handler;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ehas.auth.AuthSecurityConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {

    //@PreAuthorize("hasPermission('UserHandler:getUser', '')")
	@PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> getUser(ServerRequest request) {
    	System.out.println(">>>> getUser before");
    	log.debug(">>>> getUser before");
        long personId = Long.valueOf(request.pathVariable("id"));
        return  ServerResponse.notFound().build();
    }

    @PreAuthorize("hasPermission('UserHandler:callService', '')")
    public Mono<ServerResponse> callService(ServerRequest request) {
    	System.out.println(">>>> callService before");
    	log.debug(">>>> callService before");
        return ServerResponse.notFound().build();
    }
}