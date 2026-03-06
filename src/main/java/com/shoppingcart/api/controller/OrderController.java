package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.mapper.OrderMapper;
import com.shoppingcart.api.service.OrderDetailService;
import com.shoppingcart.api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDtos.OrderResponse> createOrder(@Valid @RequestBody OrderDtos.CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OrderDtos.FullOrderResponse> getOrder(@PathVariable Long id) {
        var orderEntity = orderService.getOrderEntity(id);
        var details = orderDetailService.getDetailsByOrder(id);
        return ResponseEntity.ok(orderMapper.toFullResponse(orderEntity, details));
    }
}
