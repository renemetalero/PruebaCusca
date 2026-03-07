package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.PaymentRegistrationRequest;
import com.shoppingcart.api.dto.PaymentRegistrationResponse;
import com.shoppingcart.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paymentOrder")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PaymentRegistrationResponse> createPayment(@RequestBody PaymentRegistrationRequest request) {
        log.info("[PaymentController] createPayment orderId={} names={}", request.getIdOrder(), request.getNames());
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PaymentRegistrationResponse> updatePayment(@RequestBody PaymentRegistrationRequest request) {
        log.info("[PaymentController] updatePayment id={} orderId={}", request.getId(), request.getIdOrder());
        return ResponseEntity.ok(paymentService.updatePayment(request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> disablePayment(@PathVariable Long id) {
        log.info("[PaymentController] disablePayment id={}", id);
        paymentService.disablePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllPayments")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PaymentRegistrationResponse>> getAllPayments() {
        log.info("[PaymentController] getAllPayments");
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/getAPayment/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PaymentRegistrationResponse> getPayment(@PathVariable Long id) {
        log.info("[PaymentController] getPayment id={}", id);
        return ResponseEntity.ok(paymentService.getPayment(id));
    }
}
