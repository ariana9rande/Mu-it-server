package com.hjh.muit.controller;

import com.hjh.muit.common.enums.AuthResult;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.EmailVerifyRequestDto;
import com.hjh.muit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 인증번호 전송 API
    @PostMapping("/email/send")
    @Operation(summary = "이메일 인증 코드 발송", requestBody = @RequestBody(description = "인증코드 수신할 메일",
                required = true,
                content = @Content(
                        mediaType = "text/plain",
                        schema = @Schema(
                                type = "string",
                                example = "hjh970422@gmail.com"
                        )
                )
        )
    )
    public String sendEmail(@org.springframework.web.bind.annotation.RequestBody @Email String email) throws MessagingException, UnsupportedEncodingException {
        authService.sendCodeEmail(email);

        return "인증번호가 전송되었습니다.";
    }

    @PostMapping("/email/verify")
    @Operation(summary = "이메일 인증 코드 검증", requestBody = @RequestBody(
                required = true,
                content = @Content(
                        schema = @Schema(implementation = EmailVerifyRequestDto.class)
                )
        )
    )
    public ResponseEntity<ApiResponseDto> verifyEmail(@Valid @org.springframework.web.bind.annotation.RequestBody EmailVerifyRequestDto req) {
        AuthResult result = authService.verifyCodeEmail(req.getEmail(), req.getCode());
        log.info("email = {}, code = {}", req.getEmail(), req.getCode());

        return switch(result) {
            case SUCCESS -> ResponseEntity.ok(ApiResponseDto.builder()
                    .code(HttpStatus.OK.value())
                    .message("인증 성공")
                    .build());

            case EXPIRED -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("인증 코드 만료")
                    .build());

            case INCORRECT -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDto.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("인증 코드 불일치")
                    .build());

            default -> throw new IllegalStateException("Unexpected value: " + result);
        };
    }
}
