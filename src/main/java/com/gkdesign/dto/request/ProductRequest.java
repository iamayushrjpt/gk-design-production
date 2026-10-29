package com.gkdesign.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gkdesign.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Name is required")
    private String name;

    private String categoryId; // optional — frontend links products by category name, not a normalized id

    @NotBlank(message = "Category is required")
    private String category;

    private String brand;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    private Double compareAtPrice;
    private String material;
    private String finish;

    @Valid
    private Product.Dimensions dimensions;
    private String weight;
    private String stockStatus;

    private List<Product.ProductColor> colors;
    private List<String> sizes;
    private List<String> images;

    private String description;
    private List<String> features;
    private String careInstructions;
    private String deliveryInfo;
    private String estimatedDeliveryDays;
    private List<Product.CustomizationOption> customizationOptions;

    private Product.ProductStatus status;
    @JsonProperty("isLuxury")
    private boolean isLuxury;

    @JsonProperty("isFeatured")
    private boolean isFeatured;

    @JsonProperty("isBestSeller")
    private boolean isBestSeller;

    @JsonProperty("isNewArrival")
    private boolean isNewArrival;
}
