package com.hjh.muit.controller;

import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.ApiResponseDto;
import com.hjh.muit.entity.dto.LoginRequestDto;
import com.hjh.muit.entity.dto.SignupRequestDto;
import com.hjh.muit.repository.UserRepository;
import com.hjh.muit.service.AuthService;
import com.hjh.muit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;



    @GetMapping("/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User user) {
        log.info("user = {}", user.getAttributes());
        return user.getAttributes();
    }
}
