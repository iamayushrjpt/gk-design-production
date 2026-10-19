package com.gkdesign.repository;

import com.gkdesign.model.WhatsAppInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WhatsAppInquiryRepository extends MongoRepository<WhatsAppInquiry, String> {
    Page<WhatsAppInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
