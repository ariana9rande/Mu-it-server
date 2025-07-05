//package com.hjh.muit.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class RefreshToken extends BaseEntity {
//
//    @Column(nullable = false, unique = true)
//    private Long userId;
//
//    @Column(nullable = false)
//    private String refreshToken;
//
//    public RefreshToken(Long userId, String refreshToken) {
//        this.userId = userId;
//        this.refreshToken = refreshToken;
//    }
//
//    public RefreshToken update(String newRefreshToken) {
//        this.refreshToken = newRefreshToken;
//        return this;
//    }
//}
