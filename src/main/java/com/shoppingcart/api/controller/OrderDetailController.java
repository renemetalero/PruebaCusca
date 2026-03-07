package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.OrderDetailRequest;
import com.shoppingcart.api.dto.OrderDetailResponse;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
@Slf4j
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDetailResponse> addDetail(@PathVariable Long orderId,
                                                         @Valid @RequestBody OrderDetailRequest request) {
        log.info("[OrderDetailController] addDetail path orderId={} productId={}", orderId, request.getProductId());
        return ResponseEntity.ok(orderDetailService.addDetail(orderId, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDetailResponse> addDetail(@Valid @RequestBody OrderDetailRequest request) {
        log.info("[OrderDetailController] addDetail body orderId={} productId={}", request.getOrderId(), request.getProductId());
        if (request.getOrderId() == null) {
            throw new BadRequestException("orderId is required");
        }
        return ResponseEntity.ok(orderDetailService.addDetail(request.getOrderId(), request));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderDetailResponse>> getDetails(@PathVariable Long orderId) {
        log.info("[OrderDetailController] getDetails orderId={}", orderId);
        return ResponseEntity.ok(orderDetailService.getDetailsByOrder(orderId));
    }
}
