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

    @GetMapping // (GET) /api/categories?root=FASHION
    public ResponseEntity<List<CategoryResponse>> getCategories(@RequestParam("root") RootCategory rootCategory) {
        List<CategoryResponse> responses = categoryAdminService.getCategoriesByRoot(rootCategory);
        return ResponseEntity.ok(responses);
    }

    @PostMapping  // (POST) /api/categories
    public ResponseEntity<Long> createCategory(@RequestBody CategoryCreateRequest request) {
        Long categoryId = categoryAdminService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @PutMapping("/batch") // (PUT) /api/categories/batch
    public ResponseEntity<Void> updateCategoriesBatch(@RequestBody List<CategoryUpdateRequest> requests) {
        categoryAdminService.updateCategoriesBatch(requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roots") // (GET) /api/categories/roots
    public ResponseEntity<List<RootCategoryResponse>> getRootCategories() {
        List<RootCategoryResponse> roots = Arrays.stream(RootCategory.values())
                .map(RootCategoryResponse::fromEnum)
                .toList();

        return ResponseEntity.ok(roots);
    }

    @DeleteMapping("/{categoryId}") // (DELETE) /api/categories/{categoryId}
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryAdminService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

}
