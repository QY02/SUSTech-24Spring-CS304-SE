package org.eventCenter.fileServer.component;

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
            log.error("Admin token not set, the server will reject all the requests");
        }
    }
}
