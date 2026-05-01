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
        log.atInfo()
                .addArgument(StructuredArguments.kv("service", event.service()))
                .addArgument(StructuredArguments.kv("component", event.component()))
                .addArgument(StructuredArguments.kv("level", event.level()))
                .addArgument(StructuredArguments.kv("message", event.message()))
                .addArgument(StructuredArguments.kv("method", event.method()))
                .addArgument(StructuredArguments.kv("url", event.url()))
                .addArgument(StructuredArguments.kv("status", event.status()))
                .addArgument(StructuredArguments.kv("duration_ms", event.durationMs()))
                .addArgument(StructuredArguments.kv("error_name", event.errorName()))
                .addArgument(StructuredArguments.kv("error_message", event.errorMessage()))
                .log("front monitoring event");

        return ResponseEntity.noContent().build();
    }
}