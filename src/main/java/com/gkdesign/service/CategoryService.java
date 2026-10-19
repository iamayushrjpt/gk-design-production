package com.gkdesign.service;

import com.gkdesign.dto.request.CategoryRequest;
import com.gkdesign.exception.DuplicateResourceException;
import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.Category;
import com.gkdesign.model.Product;
import com.gkdesign.repository.CategoryRepository;
import com.gkdesign.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getPublicCategories() {
        List<Category> categories = categoryRepository.findByIsHiddenFalseAndDeletedAtIsNullOrderByOrderAsc();
        categories.forEach(this::withLiveCount);
        return categories;
    }

    public List<Category> getAllForAdmin() {
        List<Category> categories = categoryRepository.findByDeletedAtIsNullOrderByOrderAsc();
        categories.forEach(this::withLiveCount);
        return categories;
    }

    public Category getBySlug(String slug) {
        Category category = categoryRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("No category found with slug: " + slug));
        return withLiveCount(category);
    }

    public Category create(CategoryRequest request) {
        String slug = request.getSlug() != null && !request.getSlug().isBlank()
                ? request.getSlug()
                : slugify(request.getName());

        if (categoryRepository.findBySlugAndDeletedAtIsNull(slug).isPresent()) {
            throw new DuplicateResourceException("A category with slug '" + slug + "' already exists.");
        }

        Category category = Category.builder()
                .slug(slug)
                .name(request.getName())
                .description(request.getDescription())
                .image(request.getImage())
                .icon(request.getIcon())
                .order(request.getOrder() != null ? request.getOrder() : (int) categoryRepository.count() + 1)
                .isHidden(request.isHidden())
                .parentId(request.getParentId())
                .build();

        return categoryRepository.save(category);
    }

    public Category update(String slug, CategoryRequest request) {
        Category existing = getBySlug(slug);
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setImage(request.getImage());
        existing.setIcon(request.getIcon());
        if (request.getOrder() != null) existing.setOrder(request.getOrder());
        existing.setHidden(request.isHidden());
        existing.setParentId(request.getParentId());
        return categoryRepository.save(existing);
    }

    public void softDelete(String slug) {
        Category category = getBySlug(slug);
        category.setDeletedAt(Instant.now());
        categoryRepository.save(category);
    }

    /** Sets the transient `count` field from a live product count — never stored, so it can't go stale. */
    private Category withLiveCount(Category category) {
        long count = productRepository.countByCategoryAndStatusAndDeletedAtIsNull(
                category.getName(), Product.ProductStatus.PUBLISHED);
        category.setCount((int) count);
        return category;
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w\\s-]").matcher(normalized).replaceAll("");
        return slug.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
