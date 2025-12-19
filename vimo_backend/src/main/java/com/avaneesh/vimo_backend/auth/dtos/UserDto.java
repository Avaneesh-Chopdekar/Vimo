package com.avaneesh.vimo_backend.auth.dtos;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.avaneesh.vimo_backend.auth.entities.Provider;
import com.avaneesh.vimo_backend.auth.entities.Role;
import com.avaneesh.vimo_backend.auth.entities.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private UUID id;
    private String email;
    private String name;
    private String password;
    private String image;
    @Builder.Default
    private boolean enable = true;
    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant updatedAt = Instant.now();
    @Builder.Default
    private Provider provider = Provider.LOCAL;
    @Builder.Default
    private Set<RoleDto> roles = new HashSet<>();

    public static UserDto fromEntity(User user) {
        Set<RoleDto> roleDtos = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                roleDtos.add(new RoleDto(role.getId(), role.getName()));
            }
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .image(user.getImage())
                .enable(user.isEnable())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .provider(user.getProvider())
                .roles(roleDtos)
                .build();
    }
}