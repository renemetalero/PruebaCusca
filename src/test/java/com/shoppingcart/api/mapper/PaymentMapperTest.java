package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentMapperTest {

    private final PaymentMapper mapper = new PaymentMapper();

    @Test
    void shouldMapPaymentEntityAndResponse() {
        OrderEntity order = OrderEntity.builder().id(10L).build();
        Payment payment = mapper.toEntity(order, new BigDecimal("100.00"), PaymentStatus.APPROVED);
        payment.setId(8L);

        PaymentResponse response = mapper.toResponse(payment);

        assertNotNull(payment.getProcessedAt());
        assertEquals(8L, response.getPaymentId());
        assertEquals("APPROVED", response.getStatus());
    }
}
