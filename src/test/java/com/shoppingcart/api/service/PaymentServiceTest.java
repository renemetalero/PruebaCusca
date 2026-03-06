package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.mapper.PaymentMapper;
import com.shoppingcart.api.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderDetailService orderDetailService;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void payShouldApprovePaymentWhenCardIsValid() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).build();
        Payment payment = Payment.builder().order(order).amount(new BigDecimal("100.00")).status(PaymentStatus.APPROVED).build();
        Payment saved = Payment.builder().id(5L).order(order).amount(new BigDecimal("100.00")).status(PaymentStatus.APPROVED).build();

        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(new BigDecimal("100.00"));
        when(paymentMapper.toEntity(order, new BigDecimal("100.00"), PaymentStatus.APPROVED)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(saved);
        when(paymentMapper.toResponse(saved)).thenReturn(new PaymentResponse(5L, 1L, "APPROVED", new BigDecimal("100.00")));

        PaymentResponse response = paymentService.pay(new PaymentRequest(1L, "12345678", "John"));

        assertEquals("APPROVED", response.getStatus());
        assertEquals(5L, response.getPaymentId());
    }

    @Test
    void payShouldFailWhenOrderHasNoItems() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).build();
        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(BigDecimal.ZERO);

        assertThrows(BadRequestException.class,
                () -> paymentService.pay(new PaymentRequest(1L, "12345678", "John")));
    }
}
