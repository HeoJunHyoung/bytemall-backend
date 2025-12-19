package com.example.bytemallbackend.domain.catalog.product.service;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.exception.CategoryErrorCode;
import com.example.bytemallbackend.domain.catalog.category.repository.CategoryRepository;
import com.example.bytemallbackend.domain.catalog.product.dto.request.ProductCreateRequest;
import com.example.bytemallbackend.domain.catalog.product.dto.request.ProductUpdateRequest;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductSellerDetailsResponse;
import com.example.bytemallbackend.domain.catalog.product.dto.response.ProductSellerResponse;
import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import com.example.bytemallbackend.domain.catalog.product.exception.ProductErrorCode;
import com.example.bytemallbackend.domain.catalog.product.repository.ProductRepository;
import com.example.bytemallbackend.domain.member.entity.Member;
import com.example.bytemallbackend.domain.member.exception.MemberErrorCode;
import com.example.bytemallbackend.domain.member.repository.MemberRepository;
import com.example.bytemallbackend.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSellerService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    // 판매자 판매 상품 등록
    @Transactional
    public void registerProduct(Long sellerId, ProductCreateRequest request) {

        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        Product product = Product.of(
                request.getProductName(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getDescription(),
                category,
                seller
        );

        productRepository.save(product);
    }

    // 판매 상품 수정
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        product.update(
                request.getName(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getDescription(),
                request.getStatus(),
                category
        );

        productRepository.save(product);
    }

    // 판매 상품 삭제
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    // 판매자 본인의 판매 상품 목록 조회
    public List<ProductSellerResponse> getSellerProducts(Long sellerId) {

        List<Product> sellerProducts = productRepository.findBySellerId(sellerId);

        return sellerProducts.stream()
                .map(product -> ProductSellerResponse.fromEntity(product))
                .collect(Collectors.toList());
    }

    // 판매자 본인의 판매 상품 상세 조회
    public ProductSellerDetailsResponse getSellerProductDetails(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductSellerDetailsResponse.fromEntity(product);
    }

}
