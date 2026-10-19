package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media_assets")
public class MediaAsset {

    @Id
    private String id;

    private String url;
    private String filename;
    private String folder;
    private String altText;
    private long sizeBytes;
    private String uploadedBy;

    /** R2 object key (e.g. media/furniture/2026/07/16/abc123-sofa.jpg) — needed to delete the actual file, not just this metadata record. Null for assets added by external URL rather than uploaded. */
    private String objectKey;

    @CreatedDate
    private Instant createdAt;
}
