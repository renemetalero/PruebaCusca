package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentMapper {

    public Payment toEntity(OrderEntity order, BigDecimal amount, PaymentStatus status) {
        return Payment.builder()
                .order(order)
                .amount(amount)
                .status(status)
                .processedAt(LocalDateTime.now())
                .build();
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus().name(),
                payment.getAmount()
        );
    }
}
