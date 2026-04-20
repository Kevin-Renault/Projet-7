package com.openclassroom.devops.orion.microcrm;

final class LogMessages {

    static final String APPLICATION_READY = "{}#{} is ready with activeProfiles={} and applicationName='{}'";
    static final String REST_CONFIGURATION_START = "{}#{} starts with exposed entities={} and {}";
    static final String REST_CONFIGURATION_DONE = "{}#{} completed CORS configuration with allowedOrigins='{}' and allowedMethods={}";
    static final String FIXTURE_CHECK = "{}#{} evaluated initial data loading: personCount={}, canLoad={}";
    static final String FIXTURE_LOAD_START = "{}#{} loads initial data fixture with organization='{}' and personEmail='{}'";
    static final String FIXTURE_LOAD_DONE = "{}#{} saved organization='{}' with {} person(s)";

    private LogMessages() {
    }
}