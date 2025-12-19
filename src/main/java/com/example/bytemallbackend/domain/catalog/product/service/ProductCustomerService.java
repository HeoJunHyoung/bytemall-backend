package com.example.bytemallbackend.domain.catalog.product.service;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
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
    public Page<ProductCustomerResponse> getProducts(Long categoryId, String rootCategoryName, String keyword, Pageable pageable) {

        Page<Product> products;
        boolean hasKeyword = (keyword != null && !keyword.isBlank());

        // 1. 상세 카테고리 ID가 있는 경우 (가장 우선순위 높음)
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

            if (hasKeyword) {
                // [상세 카테고리] + [키워드]
                products = productRepository.findByCategoryPathWithKeyword(category.getPath(), keyword, pageable);
            } else {
                // [상세 카테고리]
                products = productRepository.findAllByCategoryPath(category.getPath(), pageable);
            }
        }
        // 2. 대분류(RootCategory) 이름이 있는 경우
        else if (rootCategoryName != null && !rootCategoryName.isBlank()) {

            // 한글 이름("패션") or 영어 이름("FASHION") -> Enum 변환
            RootCategory root = RootCategory.findByName(rootCategoryName);

            if (root != null) {
                if (hasKeyword) {
                    // [대분류] + [키워드]
                    products = productRepository.findAllByRootCategoryAndKeyword(root, keyword, pageable);
                } else {
                    // [대분류]
                    products = productRepository.findAllByRootCategory(root, pageable);
                }
            } else {
                // 잘못된 대분류명이면 검색 결과 없음(0건) 처리
                return Page.empty(pageable);
            }
        }
        // 3. 아무 필터도 없는 경우 (전체 조회)
        else {
            if (hasKeyword) {
                // [전체] + [키워드]
                products = productRepository.findAllWithKeyword(keyword, pageable);
            } else {
                // [전체]
                products = productRepository.findAll(pageable);
            }
        }

        return products.map(ProductCustomerResponse::fromEntity);
    }

    // 상품 상세 조회
    public ProductCustomerDetailsResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductCustomerDetailsResponse.fromEntity(product);

    }

}
