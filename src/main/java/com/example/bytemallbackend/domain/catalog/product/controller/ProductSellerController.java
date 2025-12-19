package com.example.bytemallbackend.domain.catalog.product.controller;

import com.example.bytemallbackend.domain.catalog.product.dto.request.ProductCreateRequest;
import com.example.bytemallbackend.domain.catalog.product.dto.request.ProductUpdateRequest;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductSellerDetailsResponse;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductSellerResponse;
import com.example.bytemallbackend.domain.catalog.product.service.ProductSellerService;
import com.example.bytemallbackend.global.security.principal.AuthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/products")
public class ProductSellerController {

    private final ProductSellerService productSellerService;

    // 판매 상품 목록 조회
    @GetMapping
    public ResponseEntity<List<ProductSellerResponse>> getSellerProducts(@AuthenticationPrincipal AuthMember authMember) {
        List<ProductSellerResponse> productSellerResponses = productSellerService.getSellerProducts(authMember.getId());
        return ResponseEntity.ok(productSellerResponses);
    }

    // 판매 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductSellerDetailsResponse> getSellerProductDetails(@PathVariable("productId") Long productId) {
        ProductSellerDetailsResponse productSellerResponses = productSellerService.getSellerProductDetails(productId);
        return ResponseEntity.ok(productSellerResponses);
    }

    // 판매 상품 등록
    @PostMapping
    public ResponseEntity<Void> registerProduct(@AuthenticationPrincipal AuthMember authMember,
                                                @RequestBody ProductCreateRequest request) {
        productSellerService.registerProduct(authMember.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 판매 상품 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductUpdateRequest request) {
        productSellerService.updateProduct(productId, request);
        return ResponseEntity.ok().build();
    }
    
    // 판매 상품 삭제
    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productSellerService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
