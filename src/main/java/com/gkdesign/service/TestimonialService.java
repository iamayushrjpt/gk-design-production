package com.gkdesign.service;

import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.Testimonial;
import com.gkdesign.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository repository;

    public List<Testimonial> getPublic() {
        return repository.findByIsHiddenFalseOrderByOrderAsc();
    }

    public List<Testimonial> getFeatured() {
        return repository.findByIsFeaturedTrueAndIsHiddenFalseOrderByOrderAsc();
    }

    public List<Testimonial> getAllForAdmin() {
        return repository.findAll();
    }

    public Testimonial create(Testimonial testimonial) {
        return repository.save(testimonial);
    }

    public Testimonial update(String id, Testimonial updated) {
        Testimonial existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found: " + id));
        existing.setCustomerName(updated.getCustomerName());
        existing.setCustomerImage(updated.getCustomerImage());
        existing.setLocation(updated.getLocation());
        existing.setReview(updated.getReview());
        existing.setRating(updated.getRating());
        existing.setFeatured(updated.isFeatured());
        existing.setHidden(updated.isHidden());
        existing.setOrder(updated.getOrder());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
