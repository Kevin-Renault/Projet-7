package com.openclassroom.devops.orion.microcrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.logstash.logback.argument.StructuredArguments;

@RestController
@RequestMapping("/api/telemetry")
@CrossOrigin
public class FrontMonitoringController {

    private static final Logger log = LoggerFactory.getLogger(FrontMonitoringController.class);

    @PostMapping("/front-logs")
    public ResponseEntity<Void> receive(@RequestBody FrontMonitoringEvent event) {
        log.info("front monitoring event",
                StructuredArguments.kv("service", event.service()),
                StructuredArguments.kv("component", event.component()),
                StructuredArguments.kv("level", event.level()),
                StructuredArguments.kv("message", event.message()),
                StructuredArguments.kv("method", event.method()),
                StructuredArguments.kv("url", event.url()),
                StructuredArguments.kv("status", event.status()),
                StructuredArguments.kv("duration_ms", event.durationMs()),
                StructuredArguments.kv("error_name", event.errorName()),
                StructuredArguments.kv("error_message", event.errorMessage()));

        return ResponseEntity.noContent().build();
    }
}