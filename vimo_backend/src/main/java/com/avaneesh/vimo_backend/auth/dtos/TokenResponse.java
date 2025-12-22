package com.avaneesh.vimo_backend.auth.dtos;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserDto user
) {

    public static TokenResponse of(String accessToken, String tokenType, long expiresIn, UserDto user) {
        return new TokenResponse(accessToken, tokenType, expiresIn, user);
    }
}