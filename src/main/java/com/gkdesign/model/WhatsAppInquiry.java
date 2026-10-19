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
@Document(collection = "whatsapp_inquiries")
public class WhatsAppInquiry {

    @Id
    private String id;

    private String productId;
    private String productName;
    private String sku;
    private String variant;
    private String size;
    private int quantity;
    private double price;

    /** Optional context: referrer page, user agent — never PII beyond what's voluntarily typed in WhatsApp itself */
    private String sourcePage;

    @CreatedDate
    private Instant createdAt;
}
