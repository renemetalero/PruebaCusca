package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.mapper.OrderDetailMapper;
import com.shoppingcart.api.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDtos.OrderDetailResponse addDetail(Long orderId, OrderDtos.OrderDetailRequest request) {
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        OrderEntity order = orderService.getOrderEntity(orderId);
        ProductDto product = productService.getProduct(request.productId());

        OrderDetail detail = orderDetailMapper.toEntity(order, product, request.quantity());

        OrderDetail saved = orderDetailRepository.save(detail);
        return orderDetailMapper.toResponse(saved);
    }

    public List<OrderDtos.OrderDetailResponse> getDetailsByOrder(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream().map(orderDetailMapper::toResponse).toList();
    }

    public BigDecimal calculateOrderTotal(Long orderId) {
        return getDetailsByOrder(orderId).stream()
                .map(OrderDtos.OrderDetailResponse::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
