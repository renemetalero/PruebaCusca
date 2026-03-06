package com.shoppingcart.api.dto;

import com.shoppingcart.api.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDtos {

    public record CreateOrderRequest(Long clientId) {
    }

    public record OrderDetailRequest(Long productId, Integer quantity) {
    }

    public record OrderResponse(Long id, Long clientId, OrderStatus status, LocalDateTime createdAt) {
    }

    public record OrderDetailResponse(Long id, Long orderId, Long productId, String productTitle, Integer quantity,
                                      BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    public record PaymentRequest(Long orderId, String cardNumber, String cardHolder) {
    }

    public record PaymentResponse(Long paymentId, Long orderId, String status, BigDecimal amount) {
    }

    public record FullOrderResponse(OrderResponse order, List<OrderDetailResponse> details) {
    }
}
