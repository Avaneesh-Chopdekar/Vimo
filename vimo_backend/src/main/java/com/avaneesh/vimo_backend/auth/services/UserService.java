package com.avaneesh.vimo_backend.auth.services;

import com.avaneesh.vimo_backend.auth.dtos.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UUID id, UserDto userDto);

    void deleteUser(UUID id);

    UserDto getUserById(UUID id);

    Iterable<UserDto> getAllUsers();
}
