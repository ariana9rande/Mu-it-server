package com.hjh.muit.controller;

import com.hjh.muit.config.jwt.TokenProvider;
import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.AccessTokenRefreshRequestDto;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.CreateAccessTokenRequest;
import com.hjh.muit.entity.dto.CreateAccessTokenResponse;
import com.hjh.muit.service.RefreshTokenService;
import com.hjh.muit.service.TokenService;
import com.hjh.muit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @PostMapping("/token")
    public ResponseEntity<ApiResponseDto> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponseDto.success("new Access Token created", newAccessToken));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponseDto> refresh(@RequestBody AccessTokenRefreshRequestDto request) {

        String refreshToken = request.getRefreshToken();

        if (!tokenProvider.validToken(refreshToken)) {
            return ResponseEntity.ok(ApiResponseDto.error("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED));
        }

        Long userId = tokenProvider.getUserId(refreshToken);

        String savedRefreshToken = refreshTokenService.findRefreshTokenByUserId(userId);

        if (!refreshToken.equals(savedRefreshToken)) {
            return ResponseEntity.ok(ApiResponseDto.error("토큰이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED));
        }

        User user = userService.findById(userId);
        String newAccessToken = tokenProvider.generateAccessToken(user);

        return ResponseEntity.ok(ApiResponseDto.success("new Access Token created", newAccessToken));
    }
}
