package com.example.bytemallbackend.domain.catalog.category.repository;

import com.example.bytemallbackend.domain.catalog.category.entity.Category;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 1. [ADMIN] displayOrder(화면상의 카테고리 순서) 조회: 카테고리 생성 시, 가장 하단에 배치하기 위해서는 형제들의 마지막 displayOrder를 알아야 함
     */
    // 1-1. 부모가 있는 경우: 같은 부모를 가진 자식들 중 가장 큰 순서값 조회
    @Query("SELECT MAX(c.displayOrder) FROM Category c WHERE c.parent = :parent")
    Integer findMaxDisplayOrderByParent(@Param("parent") Category parent);

    // 1-2. 부모가 없는(최상위) 경우: 같은 RootCategory 그룹 내에서 가장 큰 순서값 조회
    @Query("SELECT MAX(c.displayOrder) FROM Category c WHERE c.rootCategory = :rootCategory AND c.parent IS NULL")
    Integer findMaxDisplayOrderByRootCategory(@Param("rootCategory") RootCategory rootCategory);

    /**
     * 2. [ADMIN] 특정 RootCategory에 속한 '모든' 카테고리 조회: 계층 정렬은 Service에서 수행
     */
    @Query("SELECT c FROM Category c WHERE c.rootCategory = :rootCategory ORDER BY c.path ASC, c.displayOrder ASC")
    List<Category> findAllByRootCategory(@Param("rootCategory") RootCategory rootCategory);

    /**
     * 3. [CUSTOMER] 메인 페이지 접근할 때 전체 카테고리 목록 조회
     */
    @Query("SELECT c FROM Category c ORDER BY c.depth ASC, c.displayOrder ASC")
    List<Category> findAllOrderByDepthAndDisplayOrder();
}
