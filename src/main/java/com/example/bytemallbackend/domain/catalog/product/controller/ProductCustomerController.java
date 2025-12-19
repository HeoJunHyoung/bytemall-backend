package com.example.bytemallbackend.domain.catalog.product.controller;

import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductCustomerDetailsResponse;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductCustomerResponse;
import com.example.bytemallbackend.domain.catalog.product.service.ProductCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductCustomerController {

    private final ProductCustomerService productCustomerService;

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<Page<ProductCustomerResponse>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductCustomerResponse> products = productCustomerService.getProducts(categoryId, keyword, pageable);
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductCustomerDetailsResponse> getProduct(@PathVariable("productId") Long productId) {
        ProductCustomerDetailsResponse product = productCustomerService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

}
