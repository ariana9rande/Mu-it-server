package com.hjh.muit.service;

import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.SignupRequestDto;
import com.hjh.muit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User createUser(SignupRequestDto dto) {
        User user = User.builder()
                .loginId(dto.getLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(User.UserRole.USER)
                .grade(User.UserGrade.BRONZE)
                .build();

        return userRepository.save(user);
    }
}