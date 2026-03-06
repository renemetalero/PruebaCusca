package com.shoppingcart.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class AuthDtos {

    public record RegisterRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    @Builder
    public record AuthResponse(String token, String tokenType, long expiresInSeconds) {
    }
}
