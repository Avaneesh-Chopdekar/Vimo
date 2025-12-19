package com.avaneesh.vimo_backend.auth.services.impl;

import com.avaneesh.vimo_backend.auth.dtos.UserDto;
import com.avaneesh.vimo_backend.auth.repositories.UserRepository;
import com.avaneesh.vimo_backend.auth.services.AuthService;
import com.avaneesh.vimo_backend.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        // Check if email already exists
        return userService.createUser(userDto);
    }
}
