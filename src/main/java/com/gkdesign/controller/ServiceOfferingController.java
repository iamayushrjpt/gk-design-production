package com.gkdesign.controller;

import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.model.ServiceOffering;
import com.gkdesign.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/api/services")
    public ApiResponse<List<ServiceOffering>> getPublic() {
        return ApiResponse.ok(serviceOfferingService.getPublic());
    }

    @GetMapping("/api/services/{slug}")
    public ApiResponse<ServiceOffering> getBySlug(@PathVariable String slug) {
        return ApiResponse.ok(serviceOfferingService.getBySlug(slug));
    }

    @GetMapping("/api/admin/services")
    public ApiResponse<List<ServiceOffering>> getAllForAdmin() {
        return ApiResponse.ok(serviceOfferingService.getAllForAdmin());
    }

    @PostMapping("/api/admin/services")
    public ApiResponse<ServiceOffering> create(@RequestBody ServiceOffering service) {
        return ApiResponse.ok(serviceOfferingService.create(service), "Service created.");
    }

    @PutMapping("/api/admin/services/{id}")
    public ApiResponse<ServiceOffering> update(@PathVariable String id, @RequestBody ServiceOffering service) {
        return ApiResponse.ok(serviceOfferingService.update(id, service), "Service updated.");
    }

    @DeleteMapping("/api/admin/services/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        serviceOfferingService.delete(id);
        return ApiResponse.ok(null, "Service deleted.");
    }
}
