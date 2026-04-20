package com.openclassroom.devops.orion.microcrm;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class ApplicationStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartupLogger.class);

    private final Environment environment;

    ApplicationStartupLogger(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    void onApplicationReady() {
        String currentMethodName = LogContext.currentMethodName();
        String activeProfiles = Arrays.stream(environment.getActiveProfiles())
                .collect(Collectors.joining(","));
        if (activeProfiles.isBlank()) {
            activeProfiles = "default";
        }

        String applicationName = environment.getProperty("spring.application.name", "microcrm");
        log.info(LogMessages.APPLICATION_READY, getClass().getSimpleName(), currentMethodName, activeProfiles,
                applicationName);
    }
}