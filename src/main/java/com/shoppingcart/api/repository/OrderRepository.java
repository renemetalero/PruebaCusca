package com.shoppingcart.api.repository;

import com.shoppingcart.api.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
    List<OrderEntity> findAllByEnabledTrue();
}
