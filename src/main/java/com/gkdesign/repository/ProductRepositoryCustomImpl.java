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

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public Page<Product> search(ProductFilterRequest filter, Pageable pageable) {
        List<Criteria> criteriaList = new ArrayList<>();

        // Public callers never see soft-deleted docs; admin callers explicitly opt into all statuses.
        criteriaList.add(Criteria.where("deletedAt").is(null));

        if (filter.getIncludeAllStatuses() == null || !filter.getIncludeAllStatuses()) {
            criteriaList.add(Criteria.where("status").is(Product.ProductStatus.PUBLISHED));
        }

        if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
            String regex = ".*" + java.util.regex.Pattern.quote(filter.getQuery().trim()) + ".*";
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("name").regex(regex, "i"),
                    Criteria.where("category").regex(regex, "i"),
                    Criteria.where("brand").regex(regex, "i"),
                    Criteria.where("material").regex(regex, "i"),
                    Criteria.where("sku").regex(regex, "i"),
                    Criteria.where("colors.name").regex(regex, "i")
            ));
        }

        if (filter.getCategory() != null) {
            criteriaList.add(Criteria.where("category").is(filter.getCategory()));
        }
        if (filter.getMaterial() != null) {
            criteriaList.add(Criteria.where("material").regex(filter.getMaterial(), "i"));
        }
        if (filter.getBrand() != null) {
            criteriaList.add(Criteria.where("brand").is(filter.getBrand()));
        }
        if (filter.getColor() != null) {
            criteriaList.add(Criteria.where("colors.name").regex(filter.getColor(), "i"));
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
}
