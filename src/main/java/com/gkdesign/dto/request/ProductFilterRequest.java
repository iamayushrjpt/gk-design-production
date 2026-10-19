package com.gkdesign.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest {
    private String query;          // instant search: name/category/brand/material/color/sku
    private String category;
    private String material;
    private String brand;
    private String color;
    private Double minPrice;
    private Double maxPrice;
    private Double minRating;
    private Boolean luxuryOnly;
    private Boolean affordableOnly;
    private Boolean inStockOnly;
    private Boolean includeAllStatuses; // admin use only — public API always forces PUBLISHED
}
