package com.example.bytemallbackend.domain.catalog.product.repository;

import com.example.bytemallbackend.domain.catalog.product.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}