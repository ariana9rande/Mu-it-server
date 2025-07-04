package com.hjh.muit.controller;

import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.CreateAccessTokenRequest;
import com.hjh.muit.entity.dto.CreateAccessTokenResponse;
import com.hjh.muit.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<ApiResponseDto> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponseDto.success("new Access Token created", newAccessToken));
    }
}
