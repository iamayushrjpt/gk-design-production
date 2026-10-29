package com.gkdesign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    @Indexed(unique = true)
    private String sku;

    private String name;
    private String categoryId;
    private String category; // category name, matches the frontend's field name — was "categoryName", renamed for consistency
    private String brand;

    private double price;
    private Double compareAtPrice;
    private String currency = "INR";

    private String material;
    private String finish;
    private Dimensions dimensions;
    private String weight;
    private String stockStatus; // "In Stock" | "Made to Order" | "Out of Stock"

    private List<ProductColor> colors;
    private List<String> sizes;
    private List<String> images; // plain URLs — first is the cover image, matches the frontend's convention everywhere

    private String description;
    private List<String> features;
    private String careInstructions;
    private String deliveryInfo;
    private String estimatedDeliveryDays;
    private List<CustomizationOption> customizationOptions;

    /** published | draft | hidden | hold | archived */
    private ProductStatus status;

    @JsonProperty("isLuxury")
    private boolean isLuxury;

    @JsonProperty("isFeatured")
    private boolean isFeatured;

    @JsonProperty("isBestSeller")
    private boolean isBestSeller;

    @JsonProperty("isNewArrival")
    private boolean isNewArrival;

    private double rating;
    private long ratingCount;
    private long viewCount;

    private SeoMetadata seo;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    // Soft delete
    private Instant deletedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimensions {
        private double l;
        private double w;
        private double h;
        private String unit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductColor {
        private String name;
        private String hex;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomizationOption {
        private String label;
        private List<String> options;
    }

    public enum ProductStatus {
        PUBLISHED, DRAFT, HIDDEN, HOLD, ARCHIVED
    }
}
