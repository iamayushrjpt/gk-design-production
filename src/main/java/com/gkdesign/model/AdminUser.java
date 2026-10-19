package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class AdminUser {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    /** BCrypt-hashed — never store plaintext */
    private String passwordHash;

    private Role role;
    private boolean isActive;

    @CreatedDate
    private Instant createdAt;
    private Instant lastLoginAt;

    public enum Role {
        SUPERADMIN, ADMIN, EDITOR
    }
}
