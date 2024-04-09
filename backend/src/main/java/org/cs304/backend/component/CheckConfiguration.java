package org.cs304.backend.component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckConfiguration {

    @Value("${admin-token:}")
    private String adminToken;

    @PostConstruct
    public void checkConfiguration() {
        if (adminToken.isBlank()) {
            log.warn("Admin token of file server not set, some features related to file management will be disabled");
        }
    }
}
