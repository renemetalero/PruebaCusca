package com.shoppingcart.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();
    ;

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


    @Test
    @WithMockUser(roles = {"USER"})
    void shouldUpdatePayment() throws Exception {

        PaymentRegistrationRequest request = new PaymentRegistrationRequest();
        request.setId(1L);

        PaymentRegistrationResponse response = new PaymentRegistrationResponse();
        response.setId(1L);

        when(paymentService.updatePayment(any(PaymentRegistrationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/paymentOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // -------- disablePayment --------

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDisablePayment() throws Exception {

        doNothing().when(paymentService).disablePayment(1L);

        mockMvc.perform(delete("/api/paymentOrder/delete/1"))
                .andExpect(status().isNoContent());
    }
    // -------- getAllPayments --------

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnAllPayments() throws Exception {

        PaymentRegistrationResponse response = new PaymentRegistrationResponse();
        response.setId(1L);

        when(paymentService.getAllPayments())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/paymentOrder/getAllPayments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
    // -------- getPayment --------

    @Test
    @WithMockUser(roles = {"USER"})
    void shouldReturnPaymentById() throws Exception {

        PaymentRegistrationResponse response = new PaymentRegistrationResponse();
        response.setId(1L);

        when(paymentService.getPayment(1L)).thenReturn(response);

        mockMvc.perform(get("/api/paymentOrder/getAPayment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
