package com.gkdesign.repository;

import com.gkdesign.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {
    Optional<Product> findBySlugAndDeletedAtIsNull(String slug);
    boolean existsBySku(String sku);
    Page<Product> findByStatusAndDeletedAtIsNull(Product.ProductStatus status, Pageable pageable);
    long countByCategoryAndStatusAndDeletedAtIsNull(String category, Product.ProductStatus status);
}
