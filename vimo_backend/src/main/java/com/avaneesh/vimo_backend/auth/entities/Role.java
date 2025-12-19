package com.avaneesh.vimo_backend.auth.entities;

import com.avaneesh.vimo_backend.auth.dtos.RoleDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;

    public static Role fromDto(RoleDto roleDto) {
        return Role.builder()
                .id(roleDto.id())
                .name(roleDto.name())
                .build();
    }
}