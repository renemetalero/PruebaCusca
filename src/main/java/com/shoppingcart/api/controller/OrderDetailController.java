package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.OrderDtos;
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
    public ResponseEntity<OrderDtos.OrderDetailResponse> addDetail(@PathVariable Long orderId,
                                                                   @Valid @RequestBody OrderDtos.OrderDetailRequest request) {
        return ResponseEntity.ok(orderDetailService.addDetail(orderId, request));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDtos.OrderDetailResponse> addDetail(@Valid @RequestBody OrderDtos.OrderDetailRequest request) {
        if (request.orderId() == null) {
            throw new BadRequestException("orderId is required");
        }
        return ResponseEntity.ok(orderDetailService.addDetail(request.orderId(), request));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OrderDtos.OrderDetailResponse>> getDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderDetailService.getDetailsByOrder(orderId));
    }
}
