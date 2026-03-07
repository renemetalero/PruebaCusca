package com.shoppingcart.api.serviceImpl;

import com.shoppingcart.api.dto.OrderDetailRequest;
import com.shoppingcart.api.dto.OrderDetailResponse;
import com.shoppingcart.api.dto.ProductDto;
import com.shoppingcart.api.entity.OrderDetail;
import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.mapper.OrderDetailMapper;
import com.shoppingcart.api.repository.OrderDetailRepository;
import com.shoppingcart.api.service.OrderDetailService;
import com.shoppingcart.api.service.OrderService;
import com.shoppingcart.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    public OrderDetailResponse addDetail(Long orderId, OrderDetailRequest request) {
        log.info("[OrderDetailService] addDetail orderId={} productId={} quantity={}", orderId, request.getProductId(), request.getQuantity());
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        OrderEntity order = orderService.getOrderEntity(orderId);
        ProductDto product = productService.getProduct(request.getProductId());

        OrderDetail detail = orderDetailMapper.toEntity(order, product, request.getQuantity());

        OrderDetail saved = orderDetailRepository.save(detail);
        return orderDetailMapper.toResponse(saved);
    }

    @Override
    public List<OrderDetailResponse> getDetailsByOrder(Long orderId) {
        log.info("[OrderDetailService] getDetailsByOrder orderId={}", orderId);
        return orderDetailRepository.findByOrderId(orderId).stream().map(orderDetailMapper::toResponse).toList();
    }

    @Override
    public BigDecimal calculateOrderTotal(Long orderId) {
        log.info("[OrderDetailService] calculateOrderTotal orderId={}", orderId);
        return getDetailsByOrder(orderId).stream()
                .map(OrderDetailResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
