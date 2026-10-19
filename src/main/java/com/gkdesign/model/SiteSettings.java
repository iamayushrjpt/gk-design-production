package com.gkdesign.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "settings")
public class SiteSettings {

    @Id
    private String id; // always "global"

    private String businessName;
    private String logoUrl;
    private String address;
    private String phone;
    private String whatsappNumber;
    private String email;
    private String businessHours;

    private Map<String, String> socials; // instagram, facebook, pinterest

    private List<Map<String, String>> navMenu;
    private Map<String, Object> footerContent;
}
