package com.shoppingcart.api.repository;

import com.shoppingcart.api.entity.OrderEntity;
import com.shoppingcart.api.entity.PaymentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"client"})
    List<OrderEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"client"})
    Optional<OrderEntity> findById(Long id);

    @EntityGraph(attributePaths = {"client"})
    @Query("select distinct o from OrderEntity o join Payment p on p.order.id = o.id where p.status = :status")
    List<OrderEntity> findAllByPaymentStatus(PaymentStatus status);
}
