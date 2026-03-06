package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.service.OrderDetailService;
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
class OrderDetailControllerTest {

    @Mock
    private OrderDetailService orderDetailService;

    @InjectMocks
    private OrderDetailController orderDetailController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderDetailController).build();
    }

    @Test
    void shouldAddOrderDetail() throws Exception {
        when(orderDetailService.addDetail(org.mockito.ArgumentMatchers.eq(1L), any()))
                .thenReturn(new OrderDetailResponse(1L, 1L, 1L, "P", 2, BigDecimal.TEN, new BigDecimal("20")));

        mockMvc.perform(post("/api/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idOrder":1,"idProduct":1,"quantity":2}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetOrderDetails() throws Exception {
        when(orderDetailService.getDetailsByOrder(1L))
                .thenReturn(List.of(new OrderDetailResponse(1L, 1L, 1L, "P", 2, BigDecimal.TEN, new BigDecimal("20"))));

        mockMvc.perform(get("/api/details/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
