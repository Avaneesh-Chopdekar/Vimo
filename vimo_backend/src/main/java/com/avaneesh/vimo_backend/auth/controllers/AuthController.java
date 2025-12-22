package com.avaneesh.vimo_backend.auth.controllers;

import com.avaneesh.vimo_backend.auth.dtos.LoginRequest;
import com.avaneesh.vimo_backend.auth.dtos.TokenResponse;
import com.avaneesh.vimo_backend.auth.dtos.UserDto;
import com.avaneesh.vimo_backend.auth.entities.RefreshToken;
import com.avaneesh.vimo_backend.auth.entities.User;
import com.avaneesh.vimo_backend.auth.repositories.RefreshTokenRepository;
import com.avaneesh.vimo_backend.auth.repositories.UserRepository;
import com.avaneesh.vimo_backend.auth.security.CookieService;
import com.avaneesh.vimo_backend.auth.security.JwtService;
import com.avaneesh.vimo_backend.auth.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticate(request);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = userRepository.findByEmail(request.email()).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!user.isEnable()) {
            throw new DisabledException("User account is disabled");
        }

        String jti = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenStr = jwtService.generateRefreshToken(user, jti);

        cookieService.attachCookie(response, refreshTokenStr, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, "Bearer", jwtService.getAccessTtlSeconds(), UserDto.fromEntity(user));
        return ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(LoginRequest request) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.registerUser(userDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshTokenStr,
            HttpServletResponse response
    ) {
        if (!jwtService.isRefreshToken(refreshTokenStr)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String jti = jwtService.getJti(refreshTokenStr);
        UUID userId = jwtService.getUserId(refreshTokenStr);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        if (
                storedRefreshToken.isRevoked() ||
                storedRefreshToken.getExpiresAt().isBefore(Instant.now()) ||
                !storedRefreshToken.getUser().getId().equals(userId)
        ) {
            throw new BadCredentialsException("Refresh token is invalid or expired");
        }

        storedRefreshToken.setRevoked(true);
        String newJti = UUID.randomUUID().toString();
        storedRefreshToken.setReplacedByToken(newJti);
        refreshTokenRepository.save(storedRefreshToken);

        User user = storedRefreshToken.getUser();

        RefreshToken newRefreshToken = RefreshToken.builder()
                .jti(newJti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshTokenStr = jwtService.generateRefreshToken(user, newJti);

        cookieService.attachCookie(response, newRefreshTokenStr, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);
        return ResponseEntity.ok(TokenResponse.of(newAccessToken, "Bearer", jwtService.getAccessTtlSeconds(), UserDto.fromEntity(user)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshTokenStr,
            HttpServletResponse response
    ) {
        if (!jwtService.isRefreshToken(refreshTokenStr)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String jti = jwtService.getJti(refreshTokenStr);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        if (
                storedRefreshToken.isRevoked() ||
                storedRefreshToken.getExpiresAt().isBefore(Instant.now())
        ) {
            throw new BadCredentialsException("Refresh token is invalid or expired");
        }

        storedRefreshToken.setRevoked(true);
        refreshTokenRepository.save(storedRefreshToken);

        cookieService.clearCookie(response);
        cookieService.addNoStoreHeaders(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
