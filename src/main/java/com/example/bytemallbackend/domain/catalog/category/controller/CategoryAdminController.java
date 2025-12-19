package com.example.bytemallbackend.domain.catalog.category.controller;

import com.example.bytemallbackend.domain.catalog.category.dto.request.CategoryCreateRequest;
import com.example.bytemallbackend.domain.catalog.category.dto.request.CategoryUpdateRequest;
import com.example.bytemallbackend.domain.catalog.category.dto.response.CategoryResponse;
import com.example.bytemallbackend.domain.catalog.category.dto.response.RootCategoryResponse;
import com.example.bytemallbackend.domain.catalog.category.entity.enumerate.RootCategory;
import com.example.bytemallbackend.domain.catalog.category.service.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    // 루트 카테고리(Enum) 목록 조회: 관리자가 어떤 카테고리 목록들이 있는지 보기 위한 API
    @GetMapping("/roots")
    public ResponseEntity<List<RootCategoryResponse>> getRootCategories() {
        List<RootCategoryResponse> roots = Arrays.stream(RootCategory.values())
                .map(RootCategoryResponse::fromEnum)
                .toList();

        return ResponseEntity.ok(roots);
    }

    // 카테고리 목록 조회: 루트 카테고리 하위에 있는 카테고리 목록 조회 API
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam("root") RootCategory rootCategory) {
        List<CategoryResponse> responses = categoryAdminService.getCategoriesByRoot(rootCategory);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(@RequestBody CategoryCreateRequest request) {
        Long categoryId = categoryAdminService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @PutMapping("/batch")
    public ResponseEntity<Void> updateCategoriesBatch(@RequestBody List<CategoryUpdateRequest> requests) {
        categoryAdminService.updateCategoriesBatch(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryAdminService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
