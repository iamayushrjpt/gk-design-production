package com.gkdesign.model;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    private String name;
    private String description;
    private String image; // was "bannerImage" — renamed to match the frontend's field name
    private String icon;
    private int order;
    private boolean isHidden;
    private String parentId;

    /** Computed at read time by CategoryService from the live product count — never persisted (hence @Transient), so it can't go stale in the DB. */
    @org.springframework.data.annotation.Transient
    private int count;

    private SeoMetadata seo;

    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Instant deletedAt;
}
