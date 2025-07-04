package com.hjh.muit.service;

import com.hjh.muit.entity.RefreshToken;
import com.hjh.muit.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
