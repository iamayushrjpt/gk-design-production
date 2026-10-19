package com.gkdesign.service;

import com.gkdesign.exception.ResourceNotFoundException;
import com.gkdesign.model.ServiceOffering;
import com.gkdesign.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceOfferingService {

    private final ServiceOfferingRepository repository;

    public List<ServiceOffering> getPublic() {
        return repository.findByIsHiddenFalseOrderByOrderAsc();
    }

    public ServiceOffering getBySlug(String slug) {
        return repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + slug));
    }

    public List<ServiceOffering> getAllForAdmin() {
        return repository.findAll();
    }

    public ServiceOffering create(ServiceOffering service) {
        return repository.save(service);
    }

    public ServiceOffering update(String id, ServiceOffering updated) {
        ServiceOffering existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setSubtitle(updated.getSubtitle());
        existing.setDescription(updated.getDescription());
        existing.setIcon(updated.getIcon());
        existing.setGallery(updated.getGallery());
        existing.setFeatures(updated.getFeatures());
        existing.setCtaLabel(updated.getCtaLabel());
        existing.setCtaLink(updated.getCtaLink());
        existing.setOrder(updated.getOrder());
        existing.setHidden(updated.isHidden());
        existing.setSeo(updated.getSeo());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
