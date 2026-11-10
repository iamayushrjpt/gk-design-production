package com.gkdesign.repository;

import com.gkdesign.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {
    Optional<Product> findBySlugAndDeletedAtIsNull(String slug);
    Optional<Product> findBySlugAndDeletedAtIsNotNull(String slug);
    boolean existsBySku(String sku);
    boolean existsBySlug(String slug);
}
