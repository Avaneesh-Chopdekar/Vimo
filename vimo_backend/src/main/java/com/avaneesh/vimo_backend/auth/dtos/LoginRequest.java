package com.avaneesh.vimo_backend.auth.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
