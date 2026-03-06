package com.shoppingcart.api.security;

import com.shoppingcart.api.entity.AppUser;
import com.shoppingcart.api.entity.Role;
import com.shoppingcart.api.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Test
    void shouldLoadUserByUsername() {
        AppUser user = AppUser.builder().username("john").password("pass").role(Role.ROLE_USER).build();
        when(appUserRepository.findByUsername("john")).thenReturn(Optional.of(user));

        var details = appUserDetailsService.loadUserByUsername("john");

        assertEquals("john", details.getUsername());
        assertEquals(1, details.getAuthorities().size());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(appUserRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> appUserDetailsService.loadUserByUsername("john"));
    }
}
