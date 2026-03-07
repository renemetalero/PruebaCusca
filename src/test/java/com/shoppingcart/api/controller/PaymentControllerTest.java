package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void shouldCreatePayment() throws Exception {
        when(paymentService.createPayment(any())).thenReturn(new PaymentRegistrationResponse(1L, 1L, "APPROVED", "John", "Doe", "a@a.com", "999", BigDecimal.TEN, true));

        mockMvc.perform(post("/api/paymentOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id":0,"idOrder":1,"names":"John","surnames":"Doe","email":"a@a.com","phone":"999","number_card":"12345678"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
