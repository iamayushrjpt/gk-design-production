package com.gkdesign.repository;

import com.gkdesign.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlugAndDeletedAtIsNull(String slug);
    List<Category> findByDeletedAtIsNullOrderByOrderAsc();
    List<Category> findByIsHiddenFalseAndDeletedAtIsNullOrderByOrderAsc();
}
