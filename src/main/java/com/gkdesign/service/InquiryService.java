package com.gkdesign.service;

import com.gkdesign.dto.request.InquiryRequest;
import com.gkdesign.model.WhatsAppInquiry;
import com.gkdesign.repository.WhatsAppInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final WhatsAppInquiryRepository inquiryRepository;

    public WhatsAppInquiry log(InquiryRequest request) {
        WhatsAppInquiry inquiry = WhatsAppInquiry.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .sku(request.getSku())
                .variant(request.getVariant())
                .size(request.getSize())
                .quantity(Math.max(1, request.getQuantity()))
                .price(request.getPrice())
                .sourcePage(request.getSourcePage())
                .build();

        return inquiryRepository.save(inquiry);
    }

    public Page<WhatsAppInquiry> getAll(Pageable pageable) {
        return inquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
