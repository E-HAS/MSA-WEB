package com.ehas.content.user.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehas.content.user.dto.ResponseDto;
import com.ehas.content.user.dto.UserRoleDto;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.service.UserRoleServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRoleRestController {
    private final UserRoleServiceImpt userRoleServiceImpt;

    @GetMapping("/{userId}/roles")
    public ResponseEntity<ResponseDto> getRoles(@PathVariable("userId") String userId) {
        try {
        	RoleEntity roles = userRoleServiceImpt.findRoleByUserId(userId);

            if (roles != null) {
                return ResponseEntity.ok(ResponseDto.builder()
                        .status(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(Map.of("User", roles))
                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("No roles found for user.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in getRoles: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<ResponseDto> addRole(@PathVariable("userId") String userId,
                                               @RequestBody UserRoleDto userDto) {
        try {
        	UserRoleEntity addedRole = userRoleServiceImpt.addRole(userDto);
            if (addedRole != null) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.CREATED.value())
                                .message(HttpStatus.CREATED.getReasonPhrase())
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Failed to add role.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in addRole: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{userId}/roles/{roleSeq}")
    public ResponseEntity<ResponseDto> deleteRole(@PathVariable("userId") String userId,
                                                  @PathVariable("roleSeq") Integer roleSeq) {
        try {
            boolean result = userRoleServiceImpt.deleteRoleByUserIdAndRoleSeq(userId, roleSeq);
            if (result) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .message(HttpStatus.NO_CONTENT.getReasonPhrase())
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("Failed to delete role.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in deleteRole: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
