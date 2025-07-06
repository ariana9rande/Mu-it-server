package com.hjh.muit.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {

    @Schema(description = "refresh token", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJoamg5NzA0MjJAZ21haWwuY29tIiwiaWF0IjoxNzUxNzkyMjIwLCJleHAiOjE3NTIzOTcwMjAsInN1YiI6ImhqaDk3MDQyMkBnbWFpbC5jb20iLCJpZCI6MX0.Vc3iJURkxOWpvfYejG3RHzhmZ2QTrZbWr8Z7b3OH8Ig")
    private String refreshToken;
}
