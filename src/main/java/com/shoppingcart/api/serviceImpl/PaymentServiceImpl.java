package com.shoppingcart.api.serviceImpl;

import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.OrderStatus;
import com.shoppingcart.api.entity.Payment;
import com.shoppingcart.api.entity.PaymentStatus;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.service.OrderDetailService;
import com.shoppingcart.api.service.OrderService;
import com.shoppingcart.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentRegistrationResponse createPayment(PaymentRegistrationRequest request) {
        log.info("[PaymentService] createPayment orderId={} names={}", request.getIdOrder(), request.getNames());
        if (request.getIdOrder() == null) {
            throw new BadRequestException("idOrder is required");
        }

        OrderEntity order = orderService.getOrderEntity(request.getIdOrder());
        BigDecimal totalAmount = orderDetailService.calculateOrderTotal(request.getIdOrder());
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order has no items");
        }

        PaymentStatus paymentStatus = mapStatusByCard(request.getNumber_card());

        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .status(paymentStatus)
                .payerNames(request.getNames())
                .payerSurnames(request.getSurnames())
                .payerEmail(request.getEmail())
                .payerPhone(request.getPhone())
                .processedAt(LocalDateTime.now())
                .enabled(true)
                .build();

        Payment saved = paymentRepository.save(payment);
        if (paymentStatus == PaymentStatus.APPROVED) {
            order.setStatus(OrderStatus.PAID);
            orderService.save(order);
        }
        return toResponse(saved);
    }

    @Override
    public PaymentRegistrationResponse updatePayment(PaymentRegistrationRequest request) {
        log.info("[PaymentService] updatePayment id={} orderId={}", request.getId(), request.getIdOrder());
        if (request.getId() == null) {
            throw new BadRequestException("id is required");
        }

        Payment payment = paymentRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        payment.setStatus(mapStatusByCard(request.getNumber_card()));
        payment.setPayerNames(request.getNames());
        payment.setPayerSurnames(request.getSurnames());
        payment.setPayerEmail(request.getEmail());
        payment.setPayerPhone(request.getPhone());
        payment.setProcessedAt(LocalDateTime.now());

        Payment updated = paymentRepository.save(payment);
        return toResponse(updated);
    }

    @Override
    public void disablePayment(Long id) {
        log.info("[PaymentService] disablePayment id={}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));
        payment.setEnabled(false);
        payment.setStatus(PaymentStatus.DISABLED);
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentRegistrationResponse> getAllPayments() {
        log.info("[PaymentService] getAllPayments");
        return paymentRepository.findAllByStatusOrderByProcessedAtDesc(PaymentStatus.APPROVED).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PaymentRegistrationResponse getPayment(Long id) {
        log.info("[PaymentService] getPayment id={}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));
        return toResponse(payment);
    }

    private PaymentRegistrationResponse toResponse(Payment payment) {
        return new PaymentRegistrationResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus().name(),
                payment.getPayerNames(),
                payment.getPayerSurnames(),
                payment.getPayerEmail(),
                payment.getPayerPhone(),
                payment.getAmount(),
                payment.getEnabled()
        );
    }

    private PaymentStatus mapStatusByCard(String card) {
        return card != null && card.length() >= 8 ? PaymentStatus.APPROVED : PaymentStatus.DECLINED;
    }
}
