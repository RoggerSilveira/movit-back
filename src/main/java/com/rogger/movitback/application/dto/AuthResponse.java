package com.rogger.movitback.application.dto;

public record AuthResponse(String accessToken, String refreshToken, String name, String email) {}