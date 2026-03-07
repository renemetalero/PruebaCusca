package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.serviceImpl.PaymentServiceImpl;
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

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void createPaymentShouldApproveWhenCardIsValid() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).enabled(true).build();
        Payment saved = Payment.builder().id(5L).order(order).amount(new BigDecimal("100.00")).status(PaymentStatus.APPROVED).enabled(true).build();

        PaymentRegistrationRequest req = new PaymentRegistrationRequest();
        req.setIdOrder(1L);
        req.setNumber_card("12345678");

        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(new BigDecimal("100.00"));
        when(paymentRepository.save(any())).thenReturn(saved);

        PaymentRegistrationResponse response = paymentService.createPayment(req);

        assertEquals("APPROVED", response.getPaymentStatus());
        assertEquals(5L, response.getId());
    }

    @Test
    void createPaymentShouldFailWhenOrderHasNoItems() {
        OrderEntity order = OrderEntity.builder().id(1L).status(OrderStatus.CREATED).build();
        PaymentRegistrationRequest req = new PaymentRegistrationRequest();
        req.setIdOrder(1L);
        req.setNumber_card("12345678");

        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(orderDetailService.calculateOrderTotal(1L)).thenReturn(BigDecimal.ZERO);

        assertThrows(BadRequestException.class, () -> paymentService.createPayment(req));
    }
}
