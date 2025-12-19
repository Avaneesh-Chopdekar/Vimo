package com.avaneesh.vimo_backend.auth.controllers;

import com.avaneesh.vimo_backend.auth.dtos.UserDto;
import com.avaneesh.vimo_backend.auth.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Users API")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
         return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(UUID.fromString(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(@RequestParam("id") String id, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(UUID.fromString(id), userDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam("id") String id) {
        userService.deleteUser(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
