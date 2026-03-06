package com.shoppingcart.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.api.dto.AuthDtos;
import com.shoppingcart.api.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void registerShouldReturnToken() throws Exception {
        AuthDtos.AuthResponse response = AuthDtos.AuthResponse.builder().token("token").tokenType("Bearer").expiresInSeconds(7200).build();
        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/security/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthDtos.RegisterRequest("john", "pass"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}
