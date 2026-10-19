package com.gkdesign.service;

import com.gkdesign.dto.request.ProductFilterRequest;
import com.gkdesign.dto.request.ProductRequest;
import com.gkdesign.exception.DuplicateResourceException;
import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.Product;
import com.gkdesign.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> search(ProductFilterRequest filter, Pageable pageable) {
        return productRepository.search(filter, pageable);
    }

    public Product getBySlug(String slug) {
        Product product = productRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("No product found with slug: " + slug));

        // Hold/hidden/draft/archived products are invisible to public callers.
        if (product.getStatus() != Product.ProductStatus.PUBLISHED) {
            throw new ResourceNotFoundException("No product found with slug: " + slug);
        }

        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
        return product;
    }

    public Product getBySlugForAdmin(String slug) {
        return productRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("No product found with slug: " + slug));
    }

    @Transactional
    public Product create(ProductRequest request, String adminEmail) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("A product with SKU '" + request.getSku() + "' already exists.");
        }

        String slug = slugify(request.getName());

        Product product = Product.builder()
                .slug(slug)
                .sku(request.getSku())
                .name(request.getName())
                .categoryId(request.getCategoryId())
                .category(request.getCategory())
                .brand(request.getBrand())
                .price(request.getPrice())
                .compareAtPrice(request.getCompareAtPrice())
                .material(request.getMaterial())
                .finish(request.getFinish())
                .dimensions(request.getDimensions())
                .weight(request.getWeight())
                .stockStatus(request.getStockStatus())
                .colors(request.getColors())
                .sizes(request.getSizes())
                .images(request.getImages())
                .description(request.getDescription())
                .features(request.getFeatures())
                .careInstructions(request.getCareInstructions())
                .deliveryInfo(request.getDeliveryInfo())
                .estimatedDeliveryDays(request.getEstimatedDeliveryDays())
                .customizationOptions(request.getCustomizationOptions())
                .status(request.getStatus() != null ? request.getStatus() : Product.ProductStatus.DRAFT)
                .isLuxury(request.isLuxury())
                .isFeatured(request.isFeatured())
                .isBestSeller(request.isBestSeller())
                .isNewArrival(request.isNewArrival())
                .rating(0)
                .ratingCount(0)
                .viewCount(0)
                .createdBy(adminEmail)
                .updatedBy(adminEmail)
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product update(String slug, ProductRequest request, String adminEmail) {
        Product existing = getBySlugForAdmin(slug);

        existing.setName(request.getName());
        existing.setCategoryId(request.getCategoryId());
        existing.setCategory(request.getCategory());
        existing.setBrand(request.getBrand());
        existing.setPrice(request.getPrice());
        existing.setCompareAtPrice(request.getCompareAtPrice());
        existing.setMaterial(request.getMaterial());
        existing.setFinish(request.getFinish());
        existing.setDimensions(request.getDimensions());
        existing.setWeight(request.getWeight());
        existing.setStockStatus(request.getStockStatus());
        existing.setColors(request.getColors());
        existing.setSizes(request.getSizes());
        existing.setImages(request.getImages());
        existing.setDescription(request.getDescription());
        existing.setFeatures(request.getFeatures());
        existing.setCareInstructions(request.getCareInstructions());
        existing.setDeliveryInfo(request.getDeliveryInfo());
        existing.setEstimatedDeliveryDays(request.getEstimatedDeliveryDays());
        existing.setCustomizationOptions(request.getCustomizationOptions());
        if (request.getStatus() != null) existing.setStatus(request.getStatus());
        existing.setLuxury(request.isLuxury());
        existing.setFeatured(request.isFeatured());
        existing.setBestSeller(request.isBestSeller());
        existing.setNewArrival(request.isNewArrival());
        existing.setUpdatedBy(adminEmail);

        return productRepository.save(existing);
    }

    @Transactional
    public Product updateStatus(String slug, Product.ProductStatus status, String adminEmail) {
        Product product = getBySlugForAdmin(slug);
        product.setStatus(status);
        product.setUpdatedBy(adminEmail);
        return productRepository.save(product);
    }

    @Transactional
    public Product duplicate(String slug, String adminEmail) {
        Product original = getBySlugForAdmin(slug);
        Product copy = original.toBuilder()
                .id(null)
                .slug(original.getSlug() + "-copy-" + System.currentTimeMillis())
                .sku(original.getSku() + "-COPY")
                .name(original.getName() + " (Copy)")
                .status(Product.ProductStatus.DRAFT)
                .createdBy(adminEmail)
                .updatedBy(adminEmail)
                .build();
        return productRepository.save(copy);
    }

    @Transactional
    public void softDelete(String slug, String adminEmail) {
        Product product = getBySlugForAdmin(slug);
        product.setDeletedAt(Instant.now());
        product.setUpdatedBy(adminEmail);
        productRepository.save(product);
    }

    @Transactional
    public Product restore(String slug) {
        Product product = productRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found or not deleted: " + slug));
        product.setDeletedAt(null);
        product.setStatus(Product.ProductStatus.DRAFT);
        return productRepository.save(product);
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w\\s-]").matcher(normalized).replaceAll("");
        return slug.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
