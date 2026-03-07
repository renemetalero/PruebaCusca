package com.shoppingcart.api.controller;

import com.shoppingcart.api.dto.AuthDtos;
import com.shoppingcart.api.dto.CompatDtos;
import com.shoppingcart.api.dto.*;
import com.shoppingcart.api.entity.*;
import com.shoppingcart.api.exception.BadRequestException;
import com.shoppingcart.api.exception.NotFoundException;
import com.shoppingcart.api.repository.ClientRepository;
import com.shoppingcart.api.repository.OrderDetailRepository;
import com.shoppingcart.api.repository.OrderRepository;
import com.shoppingcart.api.repository.PaymentRepository;
import com.shoppingcart.api.service.AuthService;
import com.shoppingcart.api.service.PaymentService;
import com.shoppingcart.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api-prueba-cuscatlan")
@RequiredArgsConstructor
@Slf4j
public class CompatApiController {

    private final AuthService authService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ClientRepository clientRepository;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @PostMapping("/authentication/getToken")
    public ResponseEntity<CompatDtos.TokenResponse> getToken(@RequestBody CompatDtos.TokenRequest request) {
        AuthDtos.AuthResponse response = authService.loginOrRegister(new AuthDtos.LoginRequest(request.username(), request.password()));
        return ResponseEntity.ok(new CompatDtos.TokenResponse(response.token()));
    }

    @PostMapping("/authentication/renewToken")
    public ResponseEntity<CompatDtos.TokenResponse> renewToken(@RequestBody CompatDtos.RenewTokenRequest request) {
        AuthDtos.AuthResponse response = authService.renewToken(request.token());
        return ResponseEntity.ok(new CompatDtos.TokenResponse(response.token()));
    }

    @GetMapping("/products/getAllProducts")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/products/getAProduct/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> getAProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping("/orderRegistration")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CompatDtos.OrderResponseCompat> createOrder() {
        Client client = getOrCreateDefaultClient();
        OrderEntity order = OrderEntity.builder()
                .client(client)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();
        OrderEntity saved = orderRepository.save(order);
        return ResponseEntity.ok(toCompatOrder(saved, null, null));
    }

    @PutMapping("/orderRegistration")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CompatDtos.OrderResponseCompat> updateOrder(@RequestBody CompatDtos.OrderUpsertRequest request) {
        if (request.id() == null) {
            throw new BadRequestException("id is required");
        }

        OrderEntity order = orderRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (request.customer() != null && !request.customer().isBlank()) {
            Client client = order.getClient();
            client.setFirstName(request.customer());
            client.setLastName("-");
            clientRepository.save(client);
        }

        order.setStatus(mapStatus(request.orderStatus()));
        orderRepository.save(order);

        List<OrderDetail> current = orderDetailRepository.findByOrderId(order.getId());
        orderDetailRepository.deleteAll(current);

        if (request.details() != null) {
            for (CompatDtos.OrderDetailCompat d : request.details()) {
                OrderDetail detail = OrderDetail.builder()
                        .order(order)
                        .productId(d.idProduct())
                        .productTitle("Product " + d.idProduct())
                        .quantity(d.quantity())
                        .unitPrice(d.price())
                        .build();
                orderDetailRepository.save(detail);
            }
        }

        OrderEntity updated = orderRepository.findById(order.getId()).orElseThrow();
        return ResponseEntity.ok(toCompatOrder(updated, request.paymentMethod(), request.paymentStatus()));
    }

    @DeleteMapping("/orderRegistration/delete/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orderRegistration/getAllOrders")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<CompatDtos.OrderResponseCompat>> getAllOrders() {
        log.info("[CompatApiController] getAllOrders");
        List<CompatDtos.OrderResponseCompat> response = orderRepository.findAll().stream()
                .map(order -> toCompatOrder(order, null, null))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orderRegistration/getAnOrder/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CompatDtos.OrderResponseCompat> getAnOrder(@PathVariable Long id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        return ResponseEntity.ok(toCompatOrder(order, null, null));
    }

    @PostMapping("/paymentOrder")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CompatDtos.PaymentResponseCompat> createPayment(@RequestBody CompatDtos.PaymentRequestCompat request) {
        PaymentRegistrationRequest paymentRequest = new PaymentRegistrationRequest();
        paymentRequest.setIdOrder(request.idOrder());
        paymentRequest.setNames(request.names());
        paymentRequest.setSurnames(request.surnames());
        paymentRequest.setEmail(request.email());
        paymentRequest.setPhone(request.phone());
        paymentRequest.setNumber_card(request.number_card());
        PaymentRegistrationResponse response = paymentService.createPayment(paymentRequest);
        return ResponseEntity.ok(new CompatDtos.PaymentResponseCompat(
                response.getId(),
                response.getIdOrder(),
                response.getPaymentStatus(),
                request.names(),
                request.surnames(),
                request.email(),
                request.phone()
        ));
    }

    @PutMapping("/paymentOrder")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CompatDtos.PaymentResponseCompat> updatePayment(@RequestBody CompatDtos.PaymentRequestCompat request) {
        if (request.id() == null) {
            throw new BadRequestException("id is required");
        }

        PaymentRegistrationRequest paymentRequest = new PaymentRegistrationRequest();
        paymentRequest.setId(request.id());
        paymentRequest.setIdOrder(request.idOrder());
        paymentRequest.setNames(request.names());
        paymentRequest.setSurnames(request.surnames());
        paymentRequest.setEmail(request.email());
        paymentRequest.setPhone(request.phone());
        paymentRequest.setNumber_card(request.number_card());

        PaymentRegistrationResponse updated = paymentService.updatePayment(paymentRequest);

        return ResponseEntity.ok(new CompatDtos.PaymentResponseCompat(
                updated.getId(),
                updated.getIdOrder(),
                updated.getPaymentStatus(),
                updated.getNames(),
                updated.getSurnames(),
                updated.getEmail(),
                updated.getPhone()
        ));
    }

    private Client getOrCreateDefaultClient() {
        return clientRepository.findByEmail("compat.customer@local").orElseGet(() ->
                clientRepository.save(Client.builder()
                        .firstName("Compat")
                        .lastName("Customer")
                        .email("compat.customer@local")
                        .build())
        );
    }

    private CompatDtos.OrderResponseCompat toCompatOrder(OrderEntity order, String paymentMethod, String paymentStatus) {
        List<CompatDtos.OrderDetailCompat> details = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(d -> new CompatDtos.OrderDetailCompat(d.getId(), d.getProductId(), d.getQuantity(), d.getUnitPrice()))
                .toList();

        BigDecimal total = details.stream()
                .map(d -> d.price().multiply(BigDecimal.valueOf(d.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String customer = order.getClient().getFirstName();
        return new CompatDtos.OrderResponseCompat(
                order.getId(),
                customer,
                total,
                mapOrderStatusCode(order.getStatus()),
                paymentMethod,
                paymentStatus,
                details
        );
    }

    private OrderStatus mapStatus(String statusCode) {
        if (statusCode == null) {
            return OrderStatus.CREATED;
        }
        return switch (statusCode.toUpperCase()) {
            case "P" -> OrderStatus.PAID;
            case "X", "CANCELLED" -> OrderStatus.CANCELLED;
            default -> OrderStatus.CREATED;
        };
    }

    private String mapOrderStatusCode(OrderStatus status) {
        return switch (status) {
            case PAID -> "P";
            case CANCELLED -> "X";
            default -> "C";
        };
    }

    private PaymentStatus mapPaymentStatus(String card) {
        return card != null && card.length() >= 8 ? PaymentStatus.APPROVED : PaymentStatus.DECLINED;
    }
}
