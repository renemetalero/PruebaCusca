package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.AuthDtos;
import com.shoppingcart.api.entity.AppUser;
import com.shoppingcart.api.entity.Role;
import com.shoppingcart.api.exception.ConflictException;
import com.shoppingcart.api.repository.AppUserRepository;
import com.shoppingcart.api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldCreateUserAndReturnToken() {
        when(appUserRepository.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(jwtService.generateToken("john", "ROLE_USER")).thenReturn("token");
        when(jwtService.getExpirationSeconds()).thenReturn(7200L);

        AuthDtos.AuthResponse response = authService.register(new AuthDtos.RegisterRequest("john", "password"));

        assertEquals("token", response.token());
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    void registerShouldFailWhenUsernameExists() {
        when(appUserRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> authService.register(new AuthDtos.RegisterRequest("john", "password")));
    }

    @Test
    void loginShouldReturnToken() {
        AppUser user = AppUser.builder().id(1L).username("john").password("encoded").role(Role.ROLE_USER).build();
        when(appUserRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("john", "ROLE_USER")).thenReturn("token");
        when(jwtService.getExpirationSeconds()).thenReturn(7200L);

        AuthDtos.AuthResponse response = authService.login(new AuthDtos.LoginRequest("john", "password"));

        assertEquals("token", response.token());
        verify(authenticationManager).authenticate(any());
    }
}
