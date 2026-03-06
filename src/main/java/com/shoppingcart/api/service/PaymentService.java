package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.mapper.PaymentMapper;
import com.shoppingcart.api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentResponse pay(PaymentRequest request) {
        if (request.getOrderId() == null) {
            throw new BadRequestException("orderId is required");
        }

        OrderEntity order = orderService.getOrderEntity(request.getOrderId());
        if (order.getStatus() == OrderStatus.PAID) {
            throw new BadRequestException("Order is already paid");
        }

        BigDecimal totalAmount = orderDetailService.calculateOrderTotal(request.getOrderId());
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order has no items");
        }

        boolean approved = request.getCardNumber() != null && request.getCardNumber().length() >= 8;
        PaymentStatus paymentStatus = approved ? PaymentStatus.APPROVED : PaymentStatus.DECLINED;

        Payment payment = paymentMapper.toEntity(order, totalAmount, paymentStatus);

        Payment saved = paymentRepository.save(payment);
        if (approved) {
            order.setStatus(OrderStatus.PAID);
            orderService.save(order);
        }

        return paymentMapper.toResponse(saved);
    }
}
