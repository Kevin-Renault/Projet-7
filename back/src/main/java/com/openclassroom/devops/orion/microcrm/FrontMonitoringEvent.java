package com.openclassroom.devops.orion.microcrm;

public record FrontMonitoringEvent(
        String label,
        String service,
        String component,
        String level,
        String message,
        String method,
        String url,
        Integer status,
        Long durationMs,
        String errorName,
        String errorMessage) {
}