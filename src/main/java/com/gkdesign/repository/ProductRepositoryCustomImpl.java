package com.gkdesign.repository;

import com.gkdesign.dto.request.ProductFilterRequest;
import com.gkdesign.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Product> search(ProductFilterRequest filter, Pageable pageable) {
        if (filter == null) filter = new ProductFilterRequest();

        List<Criteria> criteriaList = new ArrayList<>();
        // MongoDB matches both explicit null and absent fields with is(null).
        criteriaList.add(Criteria.where("deletedAt").is(null));

        if (!Boolean.TRUE.equals(filter.getIncludeAllStatuses())) {
            criteriaList.add(Criteria.where("status").is(Product.ProductStatus.PUBLISHED));
        }

        if (hasText(filter.getQuery())) {
            String regex = ".*" + Pattern.quote(filter.getQuery().trim()) + ".*";
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("name").regex(regex, "i"),
                    Criteria.where("category").regex(regex, "i"),
                    Criteria.where("brand").regex(regex, "i"),
                    Criteria.where("material").regex(regex, "i"),
                    Criteria.where("sku").regex(regex, "i"),
                    Criteria.where("colors.name").regex(regex, "i")
            ));
        }

        if (hasText(filter.getCategory())) {
            criteriaList.add(Criteria.where("category").regex(
                    "^" + Pattern.quote(filter.getCategory().trim()) + "$", "i"));
        }
        if (hasText(filter.getMaterial())) {
            criteriaList.add(Criteria.where("material").regex(
                    Pattern.quote(filter.getMaterial().trim()), "i"));
        }
        if (hasText(filter.getBrand())) {
            criteriaList.add(Criteria.where("brand").regex(
                    "^" + Pattern.quote(filter.getBrand().trim()) + "$", "i"));
        }
        if (hasText(filter.getColor())) {
            criteriaList.add(Criteria.where("colors.name").regex(
                    Pattern.quote(filter.getColor().trim()), "i"));
        }
        if (filter.getMinPrice() != null || filter.getMaxPrice() != null) {
            Criteria priceCriteria = Criteria.where("price");
            if (filter.getMinPrice() != null) priceCriteria = priceCriteria.gte(filter.getMinPrice());
            if (filter.getMaxPrice() != null) priceCriteria = priceCriteria.lte(filter.getMaxPrice());
            criteriaList.add(priceCriteria);
        }
        if (filter.getMinRating() != null) {
            criteriaList.add(Criteria.where("rating").gte(filter.getMinRating()));
        }
        if (Boolean.TRUE.equals(filter.getLuxuryOnly())) {
            criteriaList.add(Criteria.where("isLuxury").is(true));
        }
        if (Boolean.TRUE.equals(filter.getAffordableOnly())) {
            criteriaList.add(Criteria.where("isLuxury").is(false));
        }
        if (Boolean.TRUE.equals(filter.getInStockOnly())) {
            criteriaList.add(Criteria.where("stockStatus").is("In Stock"));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Product.class);
        query.with(pageable);
        List<Product> results = mongoTemplate.find(query, Product.class);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public long countPublishedByCategoryIgnoreCase(String category) {
        if (!hasText(category)) return 0;
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("deletedAt").is(null),
                Criteria.where("status").is(Product.ProductStatus.PUBLISHED),
                Criteria.where("category").regex("^" + Pattern.quote(category.trim()) + "$", "i")
        ));
        return mongoTemplate.count(query, Product.class);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
