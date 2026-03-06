package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderDetailMapper {

    public OrderDetail toEntity(OrderEntity order, ProductDto product, Integer quantity) {
        return OrderDetail.builder()
                .order(order)
                .productId(product.id())
                .productTitle(product.title())
                .quantity(quantity)
                .unitPrice(product.price())
                .build();
    }

    public OrderDtos.OrderDetailResponse toResponse(OrderDetail detail) {
        BigDecimal lineTotal = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
        return new OrderDtos.OrderDetailResponse(
                detail.getId(),
                detail.getOrder().getId(),
                detail.getProductId(),
                detail.getProductTitle(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                lineTotal
        );
    }
}
