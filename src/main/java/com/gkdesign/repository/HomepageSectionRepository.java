package com.gkdesign.repository;

import com.gkdesign.model.HomepageSection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HomepageSectionRepository extends MongoRepository<HomepageSection, String> {
    List<HomepageSection> findAllByOrderByOrderAsc();
    List<HomepageSection> findByIsEnabledTrueOrderByOrderAsc();
}
