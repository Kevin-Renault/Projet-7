package com.openclassroom.devops.orion.microcrm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EndpointRequestLoggingFilterTest {

    private final EndpointRequestLoggingFilter filter = new EndpointRequestLoggingFilter();
    private final Logger logger = (Logger) LoggerFactory.getLogger(EndpointRequestLoggingFilter.class);
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
    void shouldSkipExcludedEndpointsOnly() {
        MockHttpServletRequest healthRequest = new MockHttpServletRequest("GET", "/actuator/health");
        MockHttpServletRequest telemetryRequest = new MockHttpServletRequest("POST", "/api/telemetry/front-logs");
        MockHttpServletRequest businessRequest = new MockHttpServletRequest("GET", "/persons/1");

        assertTrue(filter.shouldNotFilter(healthRequest));
        assertTrue(filter.shouldNotFilter(telemetryRequest));
        assertFalse(filter.shouldNotFilter(businessRequest));
    }

    @Test
    void shouldLogRequestMethodPathStatusAndDuration() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/persons/1");
        request.setQueryString("projection=full");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> ((MockHttpServletResponse) res).setStatus(204);

        filter.doFilterInternal(request, response, chain);

        assertTrue(appender.list.stream().anyMatch(event -> event.getFormattedMessage().contains("http_request")));
        assertTrue(appender.list.stream().flatMap(event -> Arrays.stream(event.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("http_method=GET")));
        assertTrue(appender.list.stream().flatMap(event -> Arrays.stream(event.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("http_path=/persons/1?projection=full")));
        assertTrue(appender.list.stream().flatMap(event -> Arrays.stream(event.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("http_status=204")));
        assertTrue(appender.list.stream().flatMap(event -> Arrays.stream(event.getArgumentArray()))
                .anyMatch(arg -> arg.toString().contains("http_duration_ms=")));
    }
}