package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.serviceImpl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
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

    @Test
    void shouldGetAllPaymentsOnlyEnabled() {
        OrderEntity order = OrderEntity.builder().id(10L).status(OrderStatus.PAID).enabled(true).build();
        Payment approved = Payment.builder().id(1L).order(order).status(PaymentStatus.APPROVED).amount(new BigDecimal("10.00")).enabled(true).build();

        when(paymentRepository.findAllByEnabledTrueOrderByProcessedAtDesc()).thenReturn(java.util.List.of(approved));

        java.util.List<PaymentRegistrationResponse> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
        assertEquals("APPROVED", result.get(0).getPaymentStatus());
    }

    @Test
    void shouldUpdatePaymentSuccessfully() {

        PaymentRegistrationRequest request = new PaymentRegistrationRequest();
        request.setId(1L);
        request.setIdOrder(10L);
        request.setNumber_card("4111111111111111");
        request.setNames("John");
        request.setSurnames("Doe");
        request.setEmail("john@local");
        request.setPhone("123456");

        OrderEntity order = OrderEntity.builder()
                .id(10L)
                .build();

        Payment payment = Payment.builder()
                .id(1L)
                .order(order)
                .enabled(true)
                .status(PaymentStatus.APPROVED)
                .build();

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(payment);

        PaymentRegistrationResponse response =
                paymentService.updatePayment(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertNotNull(payment.getProcessedAt());

        verify(paymentRepository).save(payment);
    }

    @Test
    void shouldThrowExceptionWhenUpdatePaymentIdIsNull() {

        PaymentRegistrationRequest request = new PaymentRegistrationRequest();
        request.setNames("John");

        assertThrows(BadRequestException.class,
                () -> paymentService.updatePayment(request));
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFoundOnUpdate() {

        PaymentRegistrationRequest request = new PaymentRegistrationRequest();
        request.setId(1L);

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> paymentService.updatePayment(request));
    }

    // ---------- disablePayment ----------

    @Test
    void shouldDisablePayment() {

        Payment payment = Payment.builder()
                .id(1L)
                .enabled(true)
                .status(PaymentStatus.APPROVED)
                .build();

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        paymentService.disablePayment(1L);

        assertFalse(payment.getEnabled());
        assertEquals(PaymentStatus.DISABLED, payment.getStatus());

        verify(paymentRepository).save(payment);
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFoundOnDisable() {

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> paymentService.disablePayment(1L));
    }

    // ---------- getPayment ----------

    @Test
    void shouldReturnPaymentById() {

        OrderEntity order = OrderEntity.builder()
                .id(10L)
                .build();

        Payment payment = Payment.builder()
                .id(1L)
                .order(order)
                .enabled(true)
                .status(PaymentStatus.APPROVED)
                .build();

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        PaymentRegistrationResponse response =
                paymentService.getPayment(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {

        when(paymentRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> paymentService.getPayment(1L));
    }

}
