package com.ehas.content.user.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ehas.content.common.dto.ResponseDto;
import com.ehas.content.user.dto.RoleDto;
import com.ehas.content.user.dto.UserAccountDto;
import com.ehas.content.user.dto.UserRoleDto;
import com.ehas.content.user.entity.RoleEntity;
import com.ehas.content.user.entity.UserAccountEntity;
import com.ehas.content.user.entity.UserEntity;
import com.ehas.content.user.entity.UserRoleEntity;
import com.ehas.content.user.service.RoleServiceImpt;
import com.ehas.content.user.service.UserAccountServiceImpt;
import com.ehas.content.user.service.UserRoleServiceImpt;
import com.ehas.content.user.service.UserServiceImpt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAccountRestController {
    private final UserAccountServiceImpt userAccountServiceImpt;
    private final UserServiceImpt userServiceImpt;

    @GetMapping("/{userId}/balances")
    public ResponseEntity<ResponseDto> getBalances(@PathVariable("userId") String userId) {
        try {
        	 UserEntity userEntity = userServiceImpt.findByUserId(userId);
        	 UserAccountEntity userAccountEntity = userAccountServiceImpt.findByUserSeq(userEntity.getSeq());
        	 
            if (userAccountEntity != null) {
                return ResponseEntity.ok(ResponseDto.builder()
					                        .status(HttpStatus.OK.value())
					                        .message(HttpStatus.OK.getReasonPhrase())
					                        .data(Map.of("pay", UserAccountDto.builder()
					                        								  .balance(userAccountEntity.getBalance())
					                        								  .build()))
					                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("no balance found for user.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in balance: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
    
    @PutMapping("/{userId}/balances/Payment")
    public ResponseEntity<ResponseDto> updateBalancesForPayment(@PathVariable("userId") String userId
    												,@RequestBody UserAccountDto userAccountDto) {
        try {
        	 UserEntity userEntity = userServiceImpt.findByUserId(userId);
        	 userAccountDto.setUserSeq(userEntity.getSeq());
        	 
        	 Boolean result = userAccountServiceImpt.updateBalanceForPaymentByUserSeq(userAccountDto);
        	 
            if (result != null) {
                return ResponseEntity.ok(ResponseDto.builder()
					                        .status(HttpStatus.OK.value())
					                        .message(HttpStatus.OK.getReasonPhrase())
					                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("no balance update for user.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in balance: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
    @PutMapping("/{userId}/balances/charge")
    public ResponseEntity<ResponseDto> updateBalancesForCharge(@PathVariable("userId") String userId
    												,@RequestBody UserAccountDto userAccountDto) {
        try {
        	 UserEntity userEntity = userServiceImpt.findByUserId(userId);
        	 userAccountDto.setUserSeq(userEntity.getSeq());
        	 
        	 Boolean result = userAccountServiceImpt.updateBalanceForChargeByUserSeq(userAccountDto);
        	 
            if (result != null) {
                return ResponseEntity.ok(ResponseDto.builder()
					                        .status(HttpStatus.OK.value())
					                        .message(HttpStatus.OK.getReasonPhrase())
					                        .build());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.builder()
                                .status(HttpStatus.BAD_REQUEST.value())
                                .message("no balance update for user.")
                                .build());
            }
        } catch (Exception e) {
            log.error("Error in balance: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build());
        }
    }
}
