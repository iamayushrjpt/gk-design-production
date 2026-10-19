package com.gkdesign.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InquiryRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Product name is required")
    private String productName;

    private String sku;
    private String variant;
    private String size;
    private int quantity = 1;
    private double price;
    private String sourcePage;
}
