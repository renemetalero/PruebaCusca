package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDetailResponse> addDetail(@PathVariable Long orderId,
                                                                   @Valid @RequestBody OrderDetailRequest request) {
        return ResponseEntity.ok(orderDetailService.addDetail(orderId, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDetailResponse> addDetail(@Valid @RequestBody OrderDetailRequest request) {
        if (request.getOrderId() == null) {
            throw new BadRequestException("orderId is required");
        }
        return ResponseEntity.ok(orderDetailService.addDetail(request.getOrderId(), request));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderDetailResponse>> getDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDetailService.getDetailsByOrder(orderId));
    }
}
