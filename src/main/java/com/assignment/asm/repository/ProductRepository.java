package com.assignment.asm.repository;

import com.assignment.asm.model.Product;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM products WHERE active = true", nativeQuery = true)
    List<Product> findAllActive();
    @Query(value = "SELECT * FROM products WHERE active = true AND id = :id", nativeQuery = true)
    Optional<Product> findById(@Param("id") Long id);
}
