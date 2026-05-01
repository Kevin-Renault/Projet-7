package com.openclassroom.devops.orion.microcrm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FrontMonitoringControllerTest {

    private final FrontMonitoringController controller = new FrontMonitoringController();
    private final Logger logger = (Logger) LoggerFactory.getLogger(FrontMonitoringController.class);
    private ListAppender<ILoggingEvent> appender;

    @BeforeEach
    void setUp() {
        appender = new ListAppender<>();
        appender.start();
        logger.addAppender(appender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(appender);
    }

    @Test
    void shouldReturnNoContentAndLogMonitoringEvent() {
        FrontMonitoringEvent event = new FrontMonitoringEvent(
                "[Front]",
                "front",
                "http",
                "INFO",
                "request completed",
                "GET",
                "/persons/1",
                200,
                125L,
                null,
                null);

        ResponseEntity<Void> response = controller.receive(event);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(appender.list.stream()
                .anyMatch(loggingEvent -> loggingEvent.getFormattedMessage().contains("front monitoring event")));
        assertTrue(appender.list.stream().flatMap(loggingEvent -> Arrays.stream(loggingEvent.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("service=front")));
        assertTrue(appender.list.stream().flatMap(loggingEvent -> Arrays.stream(loggingEvent.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("url=/persons/1")));
        assertTrue(appender.list.stream().flatMap(loggingEvent -> Arrays.stream(loggingEvent.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("duration_ms=125")));
    }
}