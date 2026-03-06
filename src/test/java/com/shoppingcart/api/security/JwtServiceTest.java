package com.shoppingcart.api.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void shouldGenerateAndValidateToken() {
        JwtService jwtService = new JwtService("1234567890123456789012345678901234567890123456789012345678901234", 10);
        String token = jwtService.generateToken("john", "ROLE_USER");

        assertNotNull(token);
        assertEquals("john", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, "john"));
    }
}
