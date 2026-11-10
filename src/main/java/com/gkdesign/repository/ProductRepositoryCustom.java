package com.gkdesign.repository;

import com.gkdesign.dto.request.ProductFilterRequest;
import com.gkdesign.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductFilterRequest filter, Pageable pageable);

    long countPublishedByCategoryIgnoreCase(String category);
}
