package com.gkdesign.service;

import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.HomepageSection;
import com.gkdesign.repository.HomepageSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomepageSectionService {

    private final HomepageSectionRepository repository;

    public List<HomepageSection> getEnabledSections() {
        return repository.findByIsEnabledTrueOrderByOrderAsc();
    }

    public List<HomepageSection> getAllForAdmin() {
        return repository.findAllByOrderByOrderAsc();
    }

    public HomepageSection toggle(String id, boolean enabled) {
        HomepageSection section = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homepage section not found: " + id));
        section.setEnabled(enabled);
        return repository.save(section);
    }

    public HomepageSection updateOrder(String id, int order) {
        HomepageSection section = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homepage section not found: " + id));
        section.setOrder(order);
        return repository.save(section);
    }

    public HomepageSection updateContent(String id, java.util.Map<String, Object> content) {
        HomepageSection section = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homepage section not found: " + id));
        section.setContent(content);
        return repository.save(section);
    }
}
