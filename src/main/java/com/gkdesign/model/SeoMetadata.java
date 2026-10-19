package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeoMetadata {
    private String title;
    private String description;
    private String keywords;
    private String ogImage;
    private String canonicalUrl;
    private String schemaMarkup;
}
