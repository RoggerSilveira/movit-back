package com.rogger.movitback.application.dto;

import com.rogger.movitback.application.dto.validation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 2, max = 100) String name,

        @NotBlank @Email @Size(max = 255) String email,

        @NotBlank @StrongPassword
        @Schema(description = "Mínimo 8 caracteres, com maiúscula, minúscula, número e caractere especial", example = "MinhaSenh@123")
        String password
) {}