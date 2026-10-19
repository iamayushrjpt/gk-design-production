package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "services")
public class ServiceOffering {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    private String title;
    private String subtitle;
    private String description;
    private String icon;
    private List<String> gallery;
    private List<String> features;
    private String ctaLabel;
    private String ctaLink;
    private int order;
    private boolean isHidden;

    private SeoMetadata seo;
}
