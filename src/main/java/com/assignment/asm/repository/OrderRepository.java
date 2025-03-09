package com.assignment.asm.repository;

import com.assignment.asm.model.Order;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByBusinessKey(String businessKey);

    @Query(value = "SELECT * FROM orders WHERE user_id = :userId", nativeQuery = true)
    Optional<List<Order>> findAllOrder(@Param("userId") Long userId);
    @Query(value = "SELECT * FROM orders WHERE status = 'PENDING'", nativeQuery = true)
    Optional<List<Order>> findAllOrder();
}
