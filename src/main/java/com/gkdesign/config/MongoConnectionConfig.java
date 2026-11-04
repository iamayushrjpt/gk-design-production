package com.gkdesign.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConnectionConfig {

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoConnectionPool() {
        return builder -> {

            // Manage MongoDB connection pool
            builder.applyToConnectionPoolSettings(pool -> pool
                    .minSize(1)
                    .maxSize(30)
                    .maxConnecting(4)
                    .maxWaitTime(10, TimeUnit.SECONDS)
                    .maxConnectionIdleTime(60, TimeUnit.SECONDS)
                    .maxConnectionLifeTime(30, TimeUnit.MINUTES)
            );

            // Reduce connection establishment timeout
            builder.applyToSocketSettings(socket -> socket
                    .connectTimeout(5, TimeUnit.SECONDS)
            );

            // Fail reasonably quickly if MongoDB cannot be selected
            builder.applyToClusterSettings(cluster -> cluster
                    .serverSelectionTimeout(10, TimeUnit.SECONDS)
            );
        };
    }
}

