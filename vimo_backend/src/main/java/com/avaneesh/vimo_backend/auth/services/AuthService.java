package com.avaneesh.vimo_backend.auth.services;

import com.avaneesh.vimo_backend.auth.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
