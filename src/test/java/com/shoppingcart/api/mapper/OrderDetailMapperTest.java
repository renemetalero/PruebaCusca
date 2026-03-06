package com.shoppingcart.api.mapper;

import com.shoppingcart.api.dto.OrderDtos;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailMapperTest {

    private final OrderDetailMapper mapper = new OrderDetailMapper();

    @Test
    void shouldMapToEntityAndResponse() {
        OrderEntity order = OrderEntity.builder().id(5L).build();
        ProductDto product = new ProductDto(2L, "Shirt", new BigDecimal("20.00"), "d", "c", "i");

        OrderDetail entity = mapper.toEntity(order, product, 3);
        entity.setId(11L);

        OrderDtos.OrderDetailResponse response = mapper.toResponse(entity);

        assertEquals(11L, response.id());
        assertEquals(new BigDecimal("60.00"), response.lineTotal());
    }
}
