package com.gkdesign.controller;

import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.model.HomepageSection;
import com.gkdesign.service.HomepageSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class HomepageSectionController {

    private final HomepageSectionService sectionService;

    @GetMapping("/api/homepage-sections")
    public ApiResponse<List<HomepageSection>> getEnabled() {
        return ApiResponse.ok(sectionService.getEnabledSections());
    }

    @GetMapping("/api/admin/homepage-sections")
    public ApiResponse<List<HomepageSection>> getAllForAdmin() {
        return ApiResponse.ok(sectionService.getAllForAdmin());
    }

    @PatchMapping("/api/admin/homepage-sections/{id}/toggle")
    public ApiResponse<HomepageSection> toggle(@PathVariable String id, @RequestBody ToggleRequest body) {
        return ApiResponse.ok(sectionService.toggle(id, body.isEnabled()), "Section visibility updated.");
    }

    @PatchMapping("/api/admin/homepage-sections/{id}/order")
    public ApiResponse<HomepageSection> reorder(@PathVariable String id, @RequestBody OrderRequest body) {
        return ApiResponse.ok(sectionService.updateOrder(id, body.order()), "Section order updated.");
    }

    @PutMapping("/api/admin/homepage-sections/{id}/content")
    public ApiResponse<HomepageSection> updateContent(@PathVariable String id, @RequestBody Map<String, Object> content) {
        return ApiResponse.ok(sectionService.updateContent(id, content), "Section content updated.");
    }

    public record ToggleRequest(boolean isEnabled) {}
    public record OrderRequest(int order) {}
}
