package com.hjh.muit.controller;

import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.SignupRequestDto;
import com.hjh.muit.service.AuthService;
import com.hjh.muit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 요청", requestBody = @RequestBody(description = "회원가입 정보",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SignupRequestDto.class)
            )
        )
    )
    public ResponseEntity<ApiResponseDto> signup(@org.springframework.web.bind.annotation.RequestBody SignupRequestDto dto) {
        if (!authService.isEmailVerified(dto.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("이메일 인증 정보 없음", HttpStatus.BAD_REQUEST));
        }

        if (userService.isDuplicate(dto.getEmail())) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("이미 가입된 이메일", HttpStatus.BAD_REQUEST));
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("비밀번호 확인이 일치하지 않음", HttpStatus.BAD_REQUEST));
        }

        log.info("password = {}", dto.getPassword());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        log.info("encodedPassword = {}", encodedPassword);
        dto.setPassword(encodedPassword);

        User user = userService.createUser(dto);

        return ResponseEntity.ok(ApiResponseDto.success("회원가입 성공", user.getLoginId()));
    }
}
