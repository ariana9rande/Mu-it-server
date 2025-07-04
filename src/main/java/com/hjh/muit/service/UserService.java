package com.hjh.muit.service;

import com.hjh.muit.enums.UserGrade;
import com.hjh.muit.enums.UserRole;
import com.hjh.muit.entity.User;
import com.hjh.muit.entity.dto.SignupRequestDto;
import com.hjh.muit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .role(UserRole.USER)
                .grade(UserGrade.BRONZE)
                .build();

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}