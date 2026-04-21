package com.openclassroom.devops.orion.microcrm;

final class LogMessages {

    static final String APPLICATION_READY = "{}#{} is ready with activeProfiles={} and applicationName='{}'";
    static final String REST_CONFIGURATION_START = "{}#{} starts with exposed entities={} and {}";
    static final String REST_CONFIGURATION_DONE = "{}#{} completed CORS configuration with allowedOrigins='{}' and allowedMethods={}";
    static final String FIXTURE_CHECK = "{}#{} evaluated initial data loading: personCount={}, canLoad={}";
    static final String FIXTURE_LOAD_START = "{}#{} loads initial data fixture with organization='{}' and personEmail='{}'";
    static final String FIXTURE_LOAD_DONE = "{}#{} saved organization='{}' with {} person(s)";
    static final String PERSON_CREATE_ATTEMPT = "{}#{} create requested for firstName='{}', lastName='{}', email='{}'";
    static final String PERSON_CREATE_DONE = "{}#{} created person id={} email='{}'";
    static final String PERSON_UPDATE_ATTEMPT = "{}#{} update requested for id={} firstName='{}', lastName='{}', email='{}'";
    static final String PERSON_UPDATE_DONE = "{}#{} updated person id={} email='{}'";
    static final String PERSON_DELETE_ATTEMPT = "{}#{} delete requested for id={} email='{}'";
    static final String PERSON_DELETE_DONE = "{}#{} deleted person id={} email='{}'";
    static final String ORGANIZATION_CREATE_ATTEMPT = "{}#{} create requested for name='{}'";
    static final String ORGANIZATION_CREATE_DONE = "{}#{} created organization id={} name='{}'";
    static final String ORGANIZATION_UPDATE_ATTEMPT = "{}#{} update requested for id={} name='{}'";
    static final String ORGANIZATION_UPDATE_DONE = "{}#{} updated organization id={} name='{}'";
    static final String ORGANIZATION_DELETE_ATTEMPT = "{}#{} delete requested for id={} name='{}'";
    static final String ORGANIZATION_DELETE_DONE = "{}#{} deleted organization id={} name='{}'";

    private LogMessages() {
    }
}