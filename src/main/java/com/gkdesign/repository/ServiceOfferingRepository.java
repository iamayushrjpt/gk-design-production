package com.gkdesign.repository;

import com.gkdesign.model.ServiceOffering;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOfferingRepository extends MongoRepository<ServiceOffering, String> {
    Optional<ServiceOffering> findBySlug(String slug);
    List<ServiceOffering> findByIsHiddenFalseOrderByOrderAsc();
}
