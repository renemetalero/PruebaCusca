package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.service.OrderService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        when(orderService.createOrder(any())).thenReturn(new OrderRegistrationResponse(1L, "John", BigDecimal.TEN, "CREATED", null, null, true, List.of()));

        mockMvc.perform(post("/api/orderRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customer":"John","orderStatus":"C","details":[]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(new OrderRegistrationResponse(1L, "Rene", BigDecimal.TEN, "CREATED", null, null, true, List.of())));

        mockMvc.perform(get("/api/orderRegistration/getAllOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldGetOrder() throws Exception {
        when(orderService.getOrder(1L)).thenReturn(new OrderRegistrationResponse(1L, "John", BigDecimal.TEN, "CREATED", null, null, true, List.of()));

        mockMvc.perform(get("/api/orderRegistration/getAnOrder/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
