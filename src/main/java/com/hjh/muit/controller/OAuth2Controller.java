package com.hjh.muit.controller;

import com.hjh.muit.entity.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/success")
    public ResponseEntity<ApiResponseDto> success() {
        return ResponseEntity.ok(ApiResponseDto.success("oauth2 로그인 성공", null));
    }
}
