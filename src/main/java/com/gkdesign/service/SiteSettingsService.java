package com.gkdesign.service;

import com.gkdesign.model.SiteSettings;
import com.gkdesign.repository.SiteSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteSettingsService {

    private static final String GLOBAL_ID = "global";
    private final SiteSettingsRepository repository;

    public SiteSettings get() {
        return repository.findById(GLOBAL_ID).orElseGet(this::createDefault);
    }

    public SiteSettings update(SiteSettings updated) {
        updated.setId(GLOBAL_ID);
        return repository.save(updated);
    }

    private SiteSettings createDefault() {
        SiteSettings defaults = SiteSettings.builder()
                .id(GLOBAL_ID)
                .businessName("Gk Design")
                .whatsappNumber("919999999999")
                .email("hello@gkdesign.in")
                .address("4th Cross, Indiranagar, Bengaluru 560038")
                .businessHours("Mon–Sat, 10am–7pm")
                .build();
        return repository.save(defaults);
    }
}
