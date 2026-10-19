package com.gkdesign.controller;

import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.model.SiteSettings;
import com.gkdesign.service.SiteSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SiteSettingsController {

    private final SiteSettingsService settingsService;

    @GetMapping("/api/settings")
    public ApiResponse<SiteSettings> get() {
        return ApiResponse.ok(settingsService.get());
    }

    @PutMapping("/api/admin/settings")
    public ApiResponse<SiteSettings> update(@RequestBody SiteSettings settings) {
        return ApiResponse.ok(settingsService.update(settings), "Settings updated.");
    }
}
