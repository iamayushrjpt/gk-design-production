package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "homepage_sections")
public class HomepageSection {

    @Id
    private String id;

    private String type;   // hero | category_grid | featured_collection | testimonials | ...
    private String label;
    private int order;
    private boolean isEnabled;

    /** Flexible content payload — shape depends on `type` (e.g. hero has headline/image/CTA) */
    private Map<String, Object> content;

    @LastModifiedDate
    private Instant updatedAt;
}
