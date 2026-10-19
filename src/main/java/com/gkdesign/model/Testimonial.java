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
@Document(collection = "testimonials")
public class Testimonial {

    @Id
    private String id;

    private String customerName;
    private String customerImage;
    private String location;
    private String review;
    private int rating;
    private boolean isFeatured;
    private boolean isHidden;
    private int order;

    @CreatedDate
    private Instant createdAt;
}
