package com.assignment.asm.repository;

import com.assignment.asm.model.Order;
import com.assignment.asm.model.OrderDetail;
import com.assignment.asm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Optional<OrderDetail> findByOrderAndProductId(Order order, long product);
}
