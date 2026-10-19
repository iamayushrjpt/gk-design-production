package com.gkdesign.repository;

import com.gkdesign.dto.request.ProductFilterRequest;
import com.gkdesign.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    /** Builds a dynamic MongoDB query from whichever filters are non-null, always excluding soft-deleted docs. */
    Page<Product> search(ProductFilterRequest filter, Pageable pageable);
}
