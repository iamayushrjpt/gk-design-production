package com.gkdesign.repository;

import com.gkdesign.model.Testimonial;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TestimonialRepository extends MongoRepository<Testimonial, String> {
    List<Testimonial> findByIsHiddenFalseOrderByOrderAsc();
    List<Testimonial> findByIsFeaturedTrueAndIsHiddenFalseOrderByOrderAsc();
}
