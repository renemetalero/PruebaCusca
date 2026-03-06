package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.AuthDtos;
import com.shoppingcart.api.entity.AppUser;
import com.shoppingcart.api.entity.Role;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.ConflictException;
import com.shoppingcart.api.repository.AppUserRepository;
import com.shoppingcart.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (appUserRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username already exists");
        }

        AppUser user = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        appUserRepository.save(user);
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        return AuthDtos.AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getExpirationSeconds())
                .build();
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        AppUser user = appUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return AuthDtos.AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInSeconds(jwtService.getExpirationSeconds())
                .build();
    }
}
