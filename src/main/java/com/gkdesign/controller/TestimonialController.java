package com.gkdesign.controller;

import com.gkdesign.dto.response.ApiResponse;
import com.gkdesign.model.Testimonial;
import com.gkdesign.service.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestimonialController {

    private final TestimonialService testimonialService;

    @GetMapping("/api/testimonials")
    public ApiResponse<List<Testimonial>> getPublic() {
        return ApiResponse.ok(testimonialService.getPublic());
    }

    @GetMapping("/api/admin/testimonials")
    public ApiResponse<List<Testimonial>> getAllForAdmin() {
        return ApiResponse.ok(testimonialService.getAllForAdmin());
    }

    @PostMapping("/api/admin/testimonials")
    public ApiResponse<Testimonial> create(@RequestBody Testimonial testimonial) {
        return ApiResponse.ok(testimonialService.create(testimonial), "Testimonial created.");
    }

    @PutMapping("/api/admin/testimonials/{id}")
    public ApiResponse<Testimonial> update(@PathVariable String id, @RequestBody Testimonial testimonial) {
        return ApiResponse.ok(testimonialService.update(id, testimonial), "Testimonial updated.");
    }

    @DeleteMapping("/api/admin/testimonials/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        testimonialService.delete(id);
        return ApiResponse.ok(null, "Testimonial deleted.");
    }
}
