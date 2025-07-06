package com.hjh.muit.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Schema(description = "로그인 ID", example = "hjh")
    private String loginId;

    @Schema(description = "비밀번호", example = "1234")
    private String password;

    @Schema(description = "비밀번호 확인", example = "1234")
    private String passwordConfirm;

    @Schema(description = "사용자 이름", example = "홍재호")
    private String name;

    @Schema(description = "사용자 이메일", example = "hjh970422@gmail.com")
    private String email;

    @Schema(description = "사용자 휴대폰 번호", example = "01028369068")
    private String phone;

    @Schema(description = "사용자 주소", example = "서울")
    private String address;

    @Schema(description = "회원가입 경로", example = "local", allowableValues = {"local", "google", "kakao"})
    private String provider;

    @Schema(description = "oAuth2 로그인 시 식별자", example = "12345678", nullable = true)
    private String providerId;
}
