package com.hjh.muit.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class EmailVerifyRequestDto {

    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "인증 코드", example = "123456")
    private String code;
}

