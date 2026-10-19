package com.gkdesign.config;

import com.gkdesign.model.AdminUser;
import com.gkdesign.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin-email}")
    private String seedAdminEmail;

    @Value("${app.seed.admin-password}")
    private String seedAdminPassword;

    @Override
    public void run(String... args) {
        if (adminUserRepository.count() == 0) {
            AdminUser superadmin = AdminUser.builder()
                    .name("Gk Design Admin")
                    .email(seedAdminEmail)
                    .passwordHash(passwordEncoder.encode(seedAdminPassword))
                    .role(AdminUser.Role.SUPERADMIN)
                    .isActive(true)
                    .build();

            adminUserRepository.save(superadmin);

            log.warn("==================================================================");
            log.warn("No admin users found — seeded a default SUPERADMIN account:");
            log.warn("  Email:    {}", seedAdminEmail);
            log.warn("  Password: {}", seedAdminPassword);
            log.warn("CHANGE THIS PASSWORD IMMEDIATELY after first login in production.");
            log.warn("==================================================================");
        }
    }
}
