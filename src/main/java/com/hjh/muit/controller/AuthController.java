package com.hjh.muit.controller;

import com.hjh.muit.entity.dto.EmailVerifyRequest;
import com.hjh.muit.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 인증번호 전송 API
    @PostMapping("/email/send")
    public String sendEmail(@RequestBody(description = "인증코드 수신할 메일",
            required = true,
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(
                            type = "string",
                            example = "hjh970422@gmail.com"
                    )
            )
        ) @org.springframework.web.bind.annotation.RequestBody @Email String email) {
        authService.sendEmail(email);

        return "인증번호가 전송되었습니다.";
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid EmailVerifyRequest req) {
        boolean ok = authService.verifyEmailCode(req.getEmail(), req.getCode());
        return ok ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
