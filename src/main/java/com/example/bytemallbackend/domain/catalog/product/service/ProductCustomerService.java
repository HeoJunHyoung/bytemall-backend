package com.example.bytemallbackend.domain.catalog.product.service;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.exception.CategoryErrorCode;
import com.example.bytemallbackend.domain.catalog.category.repository.CategoryRepository;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductCustomerDetailsResponse;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductCustomerResponse;
import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCustomerService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // 상품 목록 조회
    public Page<ProductCustomerResponse> getProducts(Long categoryId, String keyword, Pageable pageable) {

        Page<Product> products = null;

        if (keyword != null && !keyword.isBlank()) {
            if (categoryId != null) { // [키워드 검색] + [카테고리 별 상품]
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
                products = productRepository.findByCategoryPathWithKeyword(category.getPath(), keyword, pageable);
            } else { // [키워드 검색] + [전체 상품]
                products = productRepository.findAllWithKeyword(keyword, pageable);
            }
        } else {
            if (categoryId != null) { // [카테고리 별 상품]
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
                products = productRepository.findAllByCategoryPath(category.getPath(), pageable);
            } else { // [전체 상품]
                products = productRepository.findAll(pageable);
            }
        }

        return products.map(product -> ProductCustomerResponse.fromEntity(product));
    }



    // 상품 상세 조회
    public ProductCustomerDetailsResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductCustomerDetailsResponse.fromEntity(product);

    }

}
