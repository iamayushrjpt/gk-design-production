package com.gkdesign.controller;

import com.gkdesign.dto.request.CategoryRequest;
import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.model.Category;
import com.gkdesign.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ApiResponse<List<Category>> getPublicCategories() {
        return ApiResponse.ok(categoryService.getPublicCategories());
    }

    @GetMapping("/api/categories/{slug}")
    public ApiResponse<Category> getBySlug(@PathVariable String slug) {
        return ApiResponse.ok(categoryService.getBySlug(slug));
    }

    @GetMapping("/api/admin/categories")
    public ApiResponse<List<Category>> getAllForAdmin() {
        return ApiResponse.ok(categoryService.getAllForAdmin());
    }

    @PostMapping("/api/admin/categories")
    public ApiResponse<Category> create(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok(categoryService.create(request), "Category created.");
    }

    @PutMapping("/api/admin/categories/{slug}")
    public ApiResponse<Category> update(@PathVariable String slug, @Valid @RequestBody CategoryRequest request) {
        return ApiResponse.ok(categoryService.update(slug, request), "Category updated.");
    }

    @DeleteMapping("/api/admin/categories/{slug}")
    public ApiResponse<Void> delete(@PathVariable String slug) {
        categoryService.softDelete(slug);
        return ApiResponse.ok(null, "Category deleted.");
    }
}
