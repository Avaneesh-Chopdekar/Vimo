package com.avaneesh.vimo_backend.common.dtos;

public record ErrorResponse(
        String message,
        int status,
        String error,
        long timestamp
) {
}
