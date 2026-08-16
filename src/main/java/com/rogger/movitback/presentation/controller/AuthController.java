package com.rogger.movitback.presentation.controller;

import com.rogger.movitback.infrastructure.config.JwtService;
import com.rogger.movitback.application.dto.AuthResponse;
import com.rogger.movitback.application.dto.LoginRequest;
import com.rogger.movitback.application.dto.RefreshRequest;
import com.rogger.movitback.application.dto.RegisterRequest;
import com.rogger.movitback.application.service.RefreshTokenService;
import com.rogger.movitback.application.service.UserService;
import com.rogger.movitback.domain.model.RefreshToken;
import com.rogger.movitback.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Registro, login e renovação de tokens")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        return ResponseEntity.ok(buildAuthResponse(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateAccessToken(user.getEmail());

        return ResponseEntity.ok(new AuthResponse(
                newAccessToken, request.refreshToken(), user.getName(), user.getEmail()));
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken(), user.getName(), user.getEmail());
    }
}