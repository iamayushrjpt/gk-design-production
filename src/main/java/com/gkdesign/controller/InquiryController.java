package com.gkdesign.controller;

import com.gkdesign.dto.request.InquiryRequest;
import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.dto.response.PagedResponse;
import com.gkdesign.model.WhatsAppInquiry;
import com.gkdesign.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/api/inquiries")
    public ApiResponse<WhatsAppInquiry> log(@Valid @RequestBody InquiryRequest request) {
        return ApiResponse.ok(inquiryService.log(request), "Inquiry logged.");
    }

    @GetMapping("/api/admin/inquiries")
    public ApiResponse<PagedResponse<WhatsAppInquiry>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ApiResponse.ok(PagedResponse.from(inquiryService.getAll(pageable)));
    }
}
