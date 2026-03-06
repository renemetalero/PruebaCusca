package com.shoppingcart.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderDetailController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderDetailService orderDetailService;

    @Test
    void shouldAddOrderDetail() throws Exception {
        when(orderDetailService.addDetail(org.mockito.ArgumentMatchers.eq(1L), any()))
                .thenReturn(new OrderDtos.OrderDetailResponse(1L, 1L, 1L, "P", 2, BigDecimal.TEN, new BigDecimal("20")));

        mockMvc.perform(post("/api/details/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderDtos.OrderDetailRequest(1L, 2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetOrderDetails() throws Exception {
        when(orderDetailService.getDetailsByOrder(1L))
                .thenReturn(List.of(new OrderDtos.OrderDetailResponse(1L, 1L, 1L, "P", 2, BigDecimal.TEN, new BigDecimal("20"))));

        mockMvc.perform(get("/api/details/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
