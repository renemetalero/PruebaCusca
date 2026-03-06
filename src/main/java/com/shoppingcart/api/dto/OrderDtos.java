package com.shoppingcart.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.shoppingcart.api.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDtos {

    public record CreateOrderRequest(@JsonAlias("idClient") Long clientId) {
    }

    public record OrderDetailRequest(@JsonAlias("idOrder") Long orderId,
                                     @JsonAlias("idProduct") Long productId,
                                     Integer quantity) {
    }

    public record OrderResponse(Long id, Long clientId, OrderStatus status, LocalDateTime createdAt) {
    }

    public record OrderDetailResponse(Long id, Long orderId, Long productId, String productTitle, Integer quantity,
                                      BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    public record PaymentRequest(@JsonAlias("idOrder") Long orderId,
                                 @JsonAlias("number_card") String cardNumber,
                                 @JsonAlias("names") String cardHolder) {
    }

    public record PaymentResponse(Long paymentId, Long orderId, String status, BigDecimal amount) {
    }

    public record FullOrderResponse(OrderResponse order, List<OrderDetailResponse> details) {
    }
}
