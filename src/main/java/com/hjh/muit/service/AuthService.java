package com.hjh.muit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class AuthService {

    private static final String EMAIL_PREFIX = "auth:email:";
    private static final String SMS_PREFIX   = "auth:sms:";
    private static final int EXPIRE_MINUTES = 3;

    private final StringRedisTemplate redisTemplate;

    private final JavaMailSender mailSender;

    public AuthService(StringRedisTemplate redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    // sms/email 인증번호 생성
    public String generateAuthenticationCode() {
        // 100000 ~ 999999
        return String.valueOf((int)(Math.random() * 900000 + 100000));
    }

    // 이메일 전송
    public void sendEmail(String to) {
        String code = generateAuthenticationCode();
        String key = EMAIL_PREFIX + to;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(EXPIRE_MINUTES));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("회원가입 인증번호");
        message.setText("인증번호는 [" + code + "] 입니다. " + EXPIRE_MINUTES + "분 내에 입력해주세요.");
        mailSender.send(message);
    }

    public boolean verifyEmailCode(String to, String input) {
        return verifyCode(EMAIL_PREFIX + to, input);
    }

    private boolean verifyCode(String key, String input) {
        String saved = redisTemplate.opsForValue().get(key);
        if (saved != null && saved.equals(input)) {
//            redisTemplate.delete(key);            // 한 번 사용 후 삭제 (강추)
            return true;
        }
        return false;
    }

}
