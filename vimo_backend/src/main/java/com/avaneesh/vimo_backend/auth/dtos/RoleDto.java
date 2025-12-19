package com.avaneesh.vimo_backend.auth.dtos;

import java.util.UUID;

public record RoleDto(
        UUID id,
        String name
) {
}
