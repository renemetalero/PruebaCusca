package com.shoppingcart.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingcart.api.dto.AuthDtos;
import com.shoppingcart.api.dto.CompatDtos;
import com.shoppingcart.api.entity.Client;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.repository.ClientRepository;
import com.shoppingcart.api.repository.OrderDetailRepository;
import com.shoppingcart.api.repository.OrderRepository;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.service.AuthService;
import com.shoppingcart.api.service.PaymentService;
import com.shoppingcart.api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CompatApiControllerTest {

    @Mock
    private AuthService authService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CompatApiController compatApiController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(compatApiController).build();
    }

    @Test
    void shouldReturnToken() throws Exception {
        when(authService.loginOrRegister(any())).thenReturn(AuthDtos.AuthResponse.builder().token("abc").tokenType("Bearer").expiresInSeconds(1).build());

        mockMvc.perform(post("/api-prueba-cuscatlan/authentication/getToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CompatDtos.TokenRequest("u", "p"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc"));
    }

    @Test
    void shouldGetOneOrder() throws Exception {
        Client client = Client.builder().id(1L).firstName("John").lastName("Doe").email("john@doe.com").build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(OrderEntity.builder().id(1L).client(client).build()));
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api-prueba-cuscatlan/orderRegistration/getAnOrder/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
