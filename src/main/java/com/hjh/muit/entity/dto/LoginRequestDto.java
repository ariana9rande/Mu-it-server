package com.hjh.muit.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @Schema(description = "로그인 ID", example = "hjh")
    private String loginId;

    @Schema(description = "비밀번호", example = "1234")
    private String password;
}
