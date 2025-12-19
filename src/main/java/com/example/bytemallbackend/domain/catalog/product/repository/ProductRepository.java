package com.example.bytemallbackend.domain.catalog.product.repository;

import com.example.bytemallbackend.domain.catalog.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /******************
     ***** SELLER *****
     ******************/
    @Query("SELECT p FROM Product p JOIN FETCH p.category c WHERE p.seller.id = :sellerId")
    List<Product> findBySellerId(@Param("sellerId") Long sellerId);

    /******************
     **** CUSTOMER ****
    ******************/
    // [전체 상품]
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category",
            countQuery = "SELECT COUNT(p) FROM Product p")
    Page<Product> findAll(Pageable pageable);

    // [카테고리 별 상품]
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category c " +
            "WHERE c.path = :categoryPath OR c.path LIKE CONCAT(:categoryPath, '/%')",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.category c " +
                    "WHERE c.path = :categoryPath OR c.path LIKE CONCAT(:categoryPath, '/%')")
    Page<Product> findAllByCategoryPath(@Param("categoryPath") String categoryPath, Pageable pageable);

    // [전체 상품] + [키워드 검색]
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category c WHERE p.name LIKE CONCAT('%', :keyword, '%')",
            countQuery = "SELECT COUNT(p) FROM Product p WHERE p.name LIKE CONCAT('%', :keyword, '%')")
    Page<Product> findAllWithKeyword(@Param("keyword") String keyword, Pageable pageable);


    // [카테고리 별 상품] + [키워드 검색]
    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category c " +
            "WHERE (c.path = :categoryPath OR c.path LIKE CONCAT(:categoryPath, '/%')) " +
            "AND p.name LIKE CONCAT('%', :keyword, '%')",
           countQuery = "SELECT COUNT(p) FROM Product p JOIN p.category c " +
                   "WHERE (c.path = :categoryPath OR c.path LIKE CONCAT(:categoryPath, '/%')) " +
                   "AND p.name LIKE CONCAT('%', :keyword, '%')")
    Page<Product> findByCategoryPathWithKeyword(@Param("categoryPath") String categoryPath, @Param("keyword") String keyword, Pageable pageable);

}
