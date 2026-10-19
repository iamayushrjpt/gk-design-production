package com.gkdesign.controller;

import com.gkdesign.dto.request.ProductFilterRequest;
import com.gkdesign.dto.request.ProductRequest;
import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.dto.response.PagedResponse;
import com.gkdesign.model.Product;
import com.gkdesign.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ---------- Public endpoints ----------

    @GetMapping("/api/products")
    public ApiResponse<PagedResponse<Product>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Boolean luxuryOnly,
            @RequestParam(required = false) Boolean affordableOnly,
            @RequestParam(required = false) Boolean inStockOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        ProductFilterRequest filter = ProductFilterRequest.builder()
                .query(query).category(category).material(material).brand(brand).color(color)
                .minPrice(minPrice).maxPrice(maxPrice).minRating(minRating)
                .luxuryOnly(luxuryOnly).affordableOnly(affordableOnly).inStockOnly(inStockOnly)
                .includeAllStatuses(false)
                .build();

        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
        return ApiResponse.ok(PagedResponse.from(productService.search(filter, pageable)));
    }

    @GetMapping("/api/products/{slug}")
    public ApiResponse<Product> getBySlug(@PathVariable String slug) {
        return ApiResponse.ok(productService.getBySlug(slug));
    }

    // ---------- Admin endpoints ----------

    @GetMapping("/api/admin/products")
    public ApiResponse<PagedResponse<Product>> adminSearch(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ProductFilterRequest filter = ProductFilterRequest.builder()
                .query(query).category(category).includeAllStatuses(true)
                .build();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ApiResponse.ok(PagedResponse.from(productService.search(filter, pageable)));
    }

    @GetMapping("/api/admin/products/{slug}")
    public ApiResponse<Product> adminGetBySlug(@PathVariable String slug) {
        return ApiResponse.ok(productService.getBySlugForAdmin(slug));
    }

    @PostMapping("/api/admin/products")
    public ApiResponse<Product> create(@Valid @RequestBody ProductRequest request, Authentication auth) {
        return ApiResponse.ok(productService.create(request, auth.getName()), "Product created.");
    }

    @PutMapping("/api/admin/products/{slug}")
    public ApiResponse<Product> update(@PathVariable String slug, @Valid @RequestBody ProductRequest request, Authentication auth) {
        return ApiResponse.ok(productService.update(slug, request, auth.getName()), "Product updated.");
    }

    @PatchMapping("/api/admin/products/{slug}/status")
    public ApiResponse<Product> updateStatus(@PathVariable String slug, @RequestBody StatusUpdateRequest body, Authentication auth) {
        return ApiResponse.ok(productService.updateStatus(slug, body.status(), auth.getName()), "Status updated.");
    }

    @PostMapping("/api/admin/products/{slug}/duplicate")
    public ApiResponse<Product> duplicate(@PathVariable String slug, Authentication auth) {
        return ApiResponse.ok(productService.duplicate(slug, auth.getName()), "Product duplicated.");
    }

    @DeleteMapping("/api/admin/products/{slug}")
    public ApiResponse<Void> delete(@PathVariable String slug, Authentication auth) {
        productService.softDelete(slug, auth.getName());
        return ApiResponse.ok(null, "Product archived (soft-deleted).");
    }

    @PostMapping("/api/admin/products/{slug}/restore")
    public ApiResponse<Product> restore(@PathVariable String slug) {
        return ApiResponse.ok(productService.restore(slug), "Product restored.");
    }

    private Sort resolveSort(String sort) {
        return switch (sort) {
            case "price-asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price-desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "best-selling" -> Sort.by(Sort.Direction.DESC, "isBestSeller", "viewCount");
            default -> Sort.by(Sort.Direction.DESC, "createdAt"); // newest
        };
    }

    public record StatusUpdateRequest(Product.ProductStatus status) {}
}
