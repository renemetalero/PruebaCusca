package com.shoppingcart.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class CompatDtos {

    public record TokenRequest(String username, String password) {
    }

    public record RenewTokenRequest(String token) {
    }

    public record TokenResponse(String token) {
    }

    public record OrderDetailCompat(Long id, Long idProduct, Integer quantity, BigDecimal price) {
    }

    public record OrderUpsertRequest(Long id,
                                     String customer,
                                     BigDecimal total,
                                     String orderStatus,
                                     String paymentMethod,
                                     String paymentStatus,
                                     List<OrderDetailCompat> details) {
    }

    public record OrderResponseCompat(Long id,
                                      String customer,
                                      BigDecimal total,
                                      String orderStatus,
                                      String paymentMethod,
                                      String paymentStatus,
                                      List<OrderDetailCompat> details) {
    }

    public record PaymentRequestCompat(Long id,
                                       Long idOrder,
                                       String names,
                                       String surnames,
                                       String email,
                                       String phone,
                                       String number_card) {
    }

    public record PaymentResponseCompat(Long id,
                                        Long idOrder,
                                        String paymentStatus,
                                        String names,
                                        String surnames,
                                        String email,
                                        String phone) {
    }
}
