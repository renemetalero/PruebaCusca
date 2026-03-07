package com.shoppingcart.api.serviceImpl;

import com.shoppingcart.api.dto.OrderRegistrationDetailRequest;
import com.shoppingcart.api.dto.OrderRegistrationRequest;
import com.shoppingcart.api.dto.OrderRegistrationResponse;
import com.shoppingcart.api.entity.*;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.repository.ClientRepository;
import com.shoppingcart.api.repository.OrderDetailRepository;
import com.shoppingcart.api.repository.OrderRepository;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderRegistrationResponse createOrder(OrderRegistrationRequest request) {
        log.info("[OrderService] createOrder customer={} status={}", request.getCustomer(), request.getOrderStatus());
        Client client = getOrCreateCustomer(request.getCustomer());

        OrderEntity order = OrderEntity.builder()
                .client(client)
                .status(mapStatus(request.getOrderStatus()))
                .createdAt(LocalDateTime.now())
                .enabled(true)
                .build();
        OrderEntity saved = orderRepository.save(order);

        saveDetails(saved, request.getDetails());

        return toResponse(saved, request.getPaymentMethod(), request.getPaymentStatus());
    }

    @Override
    public OrderRegistrationResponse updateOrder(OrderRegistrationRequest request) {
        log.info("[OrderService] updateOrder id={}", request.getId());
        if (request.getId() == null) {
            throw new BadRequestException("id is required");
        }

        OrderEntity order = getOrderEntity(request.getId());
        if (request.getCustomer() != null && !request.getCustomer().isBlank()) {
            Client client = order.getClient();
            client.setFirstName(request.getCustomer());
            client.setLastName("-");
            clientRepository.save(client);
        }

        order.setStatus(mapStatus(request.getOrderStatus()));
        orderRepository.save(order);

        List<OrderDetail> current = orderDetailRepository.findByOrderId(order.getId());
        orderDetailRepository.deleteAll(current);
        saveDetails(order, request.getDetails());

        return toResponse(order, request.getPaymentMethod(), request.getPaymentStatus());
    }

    @Override
    public void disableOrder(Long id) {
        log.info("[OrderService] disableOrder id={}", id);
        OrderEntity order = getOrderEntity(id);
        order.setEnabled(false);
        order.setStatus(OrderStatus.DISABLED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderRegistrationResponse> getAllOrders() {
        log.info("[OrderService] getAllOrders");
        return orderRepository.findAll().stream().map(o -> toResponse(o, null, null)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderRegistrationResponse getOrder(Long id) {
        log.info("[OrderService] getOrder id={}", id);
        return toResponse(getOrderEntity(id), null, null);
    }

    @Override
    public OrderEntity getOrderEntity(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    public void save(OrderEntity order) {
        orderRepository.save(order);
    }

    private void saveDetails(OrderEntity order, List<OrderRegistrationDetailRequest> details) {
        if (details == null) {
            return;
        }
        for (OrderRegistrationDetailRequest d : details) {
            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .productId(d.getIdProduct())
                    .productTitle("Product " + d.getIdProduct())
                    .quantity(d.getQuantity())
                    .unitPrice(d.getPrice())
                    .build();
            orderDetailRepository.save(detail);
        }
    }

    private Client getOrCreateCustomer(String customer) {
        if (customer == null || customer.isBlank()) {
            return clientRepository.findByEmail("compat.customer@local").orElseGet(() ->
                    clientRepository.save(Client.builder()
                            .firstName("Compat")
                            .lastName("Customer")
                            .email("compat.customer@local")
                            .build())
            );
        }

        String email = customer.trim().toLowerCase().replace(" ", ".") + "@local";
        return clientRepository.findByEmail(email).orElseGet(() ->
                clientRepository.save(Client.builder()
                        .firstName(customer)
                        .lastName("-")
                        .email(email)
                        .build())
        );
    }

    private OrderRegistrationResponse toResponse(OrderEntity order, String paymentMethod, String paymentStatus) {
        List<OrderRegistrationDetailRequest> details = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(d -> {
                    OrderRegistrationDetailRequest detail = new OrderRegistrationDetailRequest();
                    detail.setId(d.getId());
                    detail.setIdProduct(d.getProductId());
                    detail.setQuantity(d.getQuantity());
                    detail.setPrice(d.getUnitPrice());
                    return detail;
                })
                .toList();

        BigDecimal total = details.stream()
                .map(d -> d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String resolvedPaymentStatus = paymentStatus;
        String resolvedPaymentMethod = paymentMethod;
        var paymentOpt = paymentRepository.findByOrderId(order.getId());
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            resolvedPaymentStatus = payment.getStatus().name();
            if (resolvedPaymentMethod == null) {
                resolvedPaymentMethod = "CARD";
            }
        }

        return new OrderRegistrationResponse(
                order.getId(),
                order.getClient().getFirstName(),
                total,
                order.getStatus().name(),
                resolvedPaymentMethod,
                resolvedPaymentStatus,
                order.getEnabled(),
                details
        );
    }

    private OrderStatus mapStatus(String statusCode) {
        if (statusCode == null) {
            return OrderStatus.CREATED;
        }
        return switch (statusCode.toUpperCase()) {
            case "P", "PAID" -> OrderStatus.PAID;
            case "X", "CANCELLED" -> OrderStatus.CANCELLED;
            case "D", "DISABLED" -> OrderStatus.DISABLED;
            default -> OrderStatus.CREATED;
        };
    }
}
