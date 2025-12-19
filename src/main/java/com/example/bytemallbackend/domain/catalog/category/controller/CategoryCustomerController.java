package com.example.bytemallbackend.domain.catalog.category.controller;

import com.example.bytemallbackend.domain.catalog.category.dto.response.CategoryHierarchyResponse;
import com.example.bytemallbackend.domain.catalog.category.service.CategoryCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryCustomerController {

    private final CategoryCustomerService categoryCustomerService;

    @GetMapping("/tree") // GET /api/categories/tree
    public ResponseEntity<Map<String, List<CategoryHierarchyResponse>>> getCategoryTree() {
        var result = categoryCustomerService.getAllCategoriesForUser();
        return ResponseEntity.ok(result);
    }
}
