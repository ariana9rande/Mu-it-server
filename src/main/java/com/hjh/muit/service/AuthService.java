package com.hjh.muit.service;

import com.hjh.muit.common.enums.AuthResult;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Duration;

@Service
@Slf4j
public class AuthService {

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;
    @Value("${spring.mail.from}")
    private String FROM_NICKNAME;
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
    public void sendEmail(String to) throws MessagingException, UnsupportedEncodingException {
        String code = generateAuthenticationCode();
        String key = EMAIL_PREFIX + to;
        redisTemplate.opsForValue().set(key, code, Duration.ofMinutes(EXPIRE_MINUTES));

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(mime, false, "UTF-8");
        h.setFrom(FROM_EMAIL, FROM_NICKNAME);
        h.setTo(to);
        h.setSubject("회원가입 인증번호");
        h.setText("인증번호는 [" + code + "] 입니다. " + EXPIRE_MINUTES + "분 내에 입력해주세요.");
        mailSender.send(mime);

    }

    public AuthResult verifyEmailCode(String to, String input) {
        return verifyCode(EMAIL_PREFIX + to, input);
    }

    private AuthResult verifyCode(String key, String input) {
        String saved = redisTemplate.opsForValue().get(key);
        log.info("saved = {}", saved);
        // 인증 성공
        if (saved != null && saved.equals(input)) {
            redisTemplate.delete(key);
            return AuthResult.SUCCESS;
        }

        // code가 존재하지 않음
        if (saved != null) {
            return AuthResult.EXPIRED;
        }

        // code가 일치하지 않음
        if (saved == null) {
            return AuthResult.INVALID;
        }
        return AuthResult.UNKNOWN;
    }

}
