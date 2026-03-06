package com.shoppingcart.api.service;

import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.mapper.OrderDetailMapper;
import com.shoppingcart.api.repository.OrderDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;
    @Mock
    private OrderDetailMapper orderDetailMapper;

    @InjectMocks
    private OrderDetailService orderDetailService;

    @Test
    void shouldThrowWhenQuantityInvalid() {
        assertThrows(BadRequestException.class,
                () -> orderDetailService.addDetail(1L, new OrderDetailRequest(null, 1L, 0)));
    }

    @Test
    void shouldCalculateTotal() {
        OrderDetailResponse d1 = new OrderDetailResponse(1L, 1L, 1L, "A", 1, BigDecimal.TEN, BigDecimal.TEN);
        OrderDetailResponse d2 = new OrderDetailResponse(2L, 1L, 2L, "B", 1, BigDecimal.ONE, BigDecimal.ONE);
        when(orderDetailRepository.findByOrderId(1L)).thenReturn(List.of(OrderDetail.builder().build(), OrderDetail.builder().build()));
        when(orderDetailMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(d1, d2);

        BigDecimal total = orderDetailService.calculateOrderTotal(1L);

        assertEquals(new BigDecimal("11"), total);
    }

    @Test
    void shouldAddDetail() {
        OrderEntity order = OrderEntity.builder().id(1L).build();
        ProductDto product = new ProductDto(1L, "P", BigDecimal.TEN, "d", "c", "i");
        OrderDetail entity = OrderDetail.builder().id(7L).order(order).build();
        OrderDetailResponse response = new OrderDetailResponse(7L, 1L, 1L, "P", 2, BigDecimal.TEN, new BigDecimal("20"));

        when(orderService.getOrderEntity(1L)).thenReturn(order);
        when(productService.getProduct(1L)).thenReturn(product);
        when(orderDetailMapper.toEntity(order, product, 2)).thenReturn(entity);
        when(orderDetailRepository.save(entity)).thenReturn(entity);
        when(orderDetailMapper.toResponse(entity)).thenReturn(response);

        OrderDetailResponse result = orderDetailService.addDetail(1L, new OrderDetailRequest(null, 1L, 2));

        assertEquals(7L, result.getId());
    }
}
