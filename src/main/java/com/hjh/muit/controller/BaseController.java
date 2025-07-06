package com.hjh.muit.controller;

import com.hjh.muit.config.jwt.TokenProvider;
import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.LoginRequestDto;
import com.hjh.muit.entity.dto.SignupRequestDto;
import com.hjh.muit.repository.UserRepository;
import com.hjh.muit.service.AuthService;
import com.hjh.muit.service.RefreshTokenService;
import com.hjh.muit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

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

        if (dto.getProvider() == null || dto.getProvider().isBlank()) {
            dto.setProvider("local");
        }

        User user = userService.createUser(dto);

        return ResponseEntity.ok(ApiResponseDto.success("회원가입 성공", user.getLoginId()));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 요청", requestBody = @RequestBody(description = "로그인 정보",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequestDto.class)
            )
    )
    )
    public ResponseEntity<ApiResponseDto> login(@org.springframework.web.bind.annotation.RequestBody LoginRequestDto dto) {

        Optional<User> user = userRepository.findByLoginId(dto.getLoginId());

        Map<String, String> dataMap = new HashMap<>();

        if (user.isPresent() && passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
            String accessToken = tokenProvider.generateAccessToken(user.get());
            String refreshToken = tokenProvider.generateRefreshToken(user.get());
            refreshTokenService.saveRefreshToken(user.get().getId(), refreshToken);

            userService.updateLastLogin(user.get().getId());

            dataMap.put("userId", user.get().getLoginId());
            dataMap.put("accessToken", accessToken);

            log.info("userId = {}", user.get().getLoginId());
            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            return ResponseEntity.ok(ApiResponseDto.success("로그인 성공", dataMap));
        }

        return ResponseEntity.ok(ApiResponseDto.error("로그인 실패", HttpStatus.BAD_REQUEST));
    }
}
