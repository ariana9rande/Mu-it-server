package com.hjh.muit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    public AuthService(RedisTemplate<String, String> redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    // sms/email 인증번호 생성
    public String generateAuthenticationCode() {
        // 100000 ~ 999999
        return String.valueOf((int)(Math.random() * 900000 + 100000));
    }

    public void saveCode(String type, String to, String code) {
        String key = type.toUpperCase() + ":" + to;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(3));
    }

    // 이메일 전송
    public void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("회원가입 인증번호");
        message.setText("인증번호는 [" + code + "] 입니다. 3분 내에 입력해주세요.");
        mailSender.send(message);
    }

}
