package com.avaneesh.vimo_backend.auth.controllers;

import com.avaneesh.vimo_backend.auth.dtos.UserDto;
import com.avaneesh.vimo_backend.auth.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }
}
