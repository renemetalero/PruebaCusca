package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.OrderRegistrationRequest;
import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderRegistration")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderRegistrationResponse> createOrder(@RequestBody OrderRegistrationRequest request) {
        log.info("[OrderController] createOrder customer={} status={}", request.getCustomer(), request.getOrderStatus());
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderRegistrationResponse> updateOrder(@RequestBody OrderRegistrationRequest request) {
        log.info("[OrderController] updateOrder id={}", request.getId());
        return ResponseEntity.ok(orderService.updateOrder(request));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> disableOrder(@PathVariable Long id) {
        log.info("[OrderController] disableOrder id={}", id);
        orderService.disableOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllOrders")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderRegistrationResponse>> getAllOrders() {
        log.info("[OrderController] getAllOrders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/getAnOrder/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderRegistrationResponse> getOrder(@PathVariable Long id) {
        log.info("[OrderController] getOrder id={}", id);
        return ResponseEntity.ok(orderService.getOrder(id));
    }
}
