package com.hjh.muit.controller;

import com.hjh.muit.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 인증번호 전송 API
    @PostMapping("/send-code")
    public String sendCode(@RequestParam String type, @RequestParam String to) {
        // 1. 인증번호 생성
        String code = authService.generateAuthenticationCode();

        // 2. Redis에 인증번호 저장
        authService.saveCode(type, to, code);

        // 3. 인증번호 전송 (이메일 전송 예시)
        if ("email".equalsIgnoreCase(type)) {
            authService.sendEmail(to, code);
        } else if ("sms".equalsIgnoreCase(type)) {
            // SMS 전송 구현 시 추가
            // authService.sendSms(to, code);
        }

        return "인증번호가 전송되었습니다.";
    }
}
