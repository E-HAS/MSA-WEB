package com.ehas.content.user.api;

import java.time.Duration;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ehas.content.user.dto.ResponseDto;
import com.ehas.content.user.dto.UserDetail;
import com.ehas.content.user.dto.UserDto;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.jwt.dto.JwtToken;
import com.ehas.content.user.jwt.service.UserJwtTokenService;
import com.ehas.content.user.redis.service.UserJwtRedisSerivceImpt;
import com.ehas.content.user.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpt userServiceImpt;
    private final UserJwtTokenService userJwtTokenService;
    private final UserJwtRedisSerivceImpt userJwtRedisSerivceImpt;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<ResponseDto> registerUser(@RequestBody UserDto userDto) {
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            boolean result = userServiceImpt.add(userDto);

            if (result) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.CREATED.value())
                                .message(HttpStatus.CREATED.getReasonPhrase())
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                throw new RuntimeException("No cookies found");
            }
            String refreshToken = null;
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
            if (refreshToken == null) {
                throw new RuntimeException("Refresh token not found in cookies.");
            }
            
            boolean deleted = userJwtRedisSerivceImpt.deleteRefreshToken(refreshToken);
            if(!deleted){
            	throw new RuntimeException("Refresh token not found in cookies.");
            }
             
            Cookie deleteCookie = new Cookie("refreshToken", "");
            deleteCookie.setHttpOnly(true);
            deleteCookie.setSecure(true);
            deleteCookie.setPath("/");
            deleteCookie.setMaxAge(0); // 즉시 만료
            // SameSite 설정은 Servlet API 4.0 이상 또는 별도 필터 필요 (생략 가능)
            response.addCookie(deleteCookie);

            String accessToken = userJwtTokenService.resolveAccessToken(request);
            if(accessToken != null) {
            	userJwtTokenService.addBlacklist(accessToken);
            }else {
            	throw new RuntimeException("Access token not found");
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.NO_CONTENT.value())
                            .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable("userId") String userId) {
        try {
            UserDetail user = userServiceImpt.findUserDetailByUserId(userId);
            if (user != null) {
                return ResponseEntity.ok(ResponseDto.builder()
                        .status(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(Map.of("user", user))
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDto> loginUser(@PathVariable("userId") String userId,
                                                  @RequestBody UserDto userDto,
                                                  HttpServletResponse response) {
    	log.info("POST USERS/USERID");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getId(), userDto.getPassword()));
            log.info("POST USERS/USERID Authentication");
            // JWT 토큰 생성 (accessToken, refreshToken)
            JwtToken token = userJwtTokenService.createJwtToken(authentication);
            log.info("POST USERS/USERID JwtToken");
            // Authorization 헤더에 accessToken 추가
            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken());

            // HttpOnly Secure 쿠키에 refreshToken 추가
            Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) Duration.ofDays(7).getSeconds());
            // SameSite 속성은 Servlet API가 지원하지 않으면 별도 설정 필요 (생략 가능)
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(HttpStatus.CREATED.getReasonPhrase())
                            .build());

        } catch (Exception e) {
        	log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .build());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto> updateUser(@PathVariable("userId") String userId,
                                                  @RequestBody UserDto userDto) {
        try {
            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            boolean updated = userServiceImpt.updateBySeq(userDto);
            if (updated) {
                return ResponseEntity.ok(ResponseDto.builder()
                        .status(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable("userId") String userId) {
        try {
            boolean deleted = userServiceImpt.delete(userId);
            if (deleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .build());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
