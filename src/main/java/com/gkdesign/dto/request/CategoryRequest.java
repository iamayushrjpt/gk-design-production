package com.gkdesign.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String slug;
    private String description;
    private String image;
    private String icon;
    private Integer order;
    private boolean isHidden;
    private String parentId;
}
