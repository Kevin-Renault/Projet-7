package com.openclassroom.devops.orion.microcrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
class RepositoryOperationLoggingHandler {

    private static final Logger log = LoggerFactory.getLogger(RepositoryOperationLoggingHandler.class);

    @HandleBeforeCreate
    public void beforeCreate(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_CREATE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                person.getFirstName(),
                person.getLastName(),
                person.getEmail());
    }

    @HandleAfterCreate
    public void afterCreate(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_CREATE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                person.getId(),
                person.getEmail());
    }

    @HandleBeforeSave
    public void beforeSave(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_UPDATE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getEmail());
    }

    @HandleAfterSave
    public void afterSave(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_UPDATE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                person.getId(),
                person.getEmail());
    }

    @HandleBeforeDelete
    public void beforeDelete(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_DELETE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                person.getId(),
                person.getEmail());
    }

    @HandleAfterDelete
    public void afterDelete(Person person) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.PERSON_DELETE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                person.getId(),
                person.getEmail());
    }

    @HandleBeforeCreate
    public void beforeCreate(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_CREATE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getName());
    }

    @HandleAfterCreate
    public void afterCreate(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_CREATE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getId(),
                organization.getName());
    }

    @HandleBeforeSave
    public void beforeSave(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_UPDATE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getId(),
                organization.getName());
    }

    @HandleAfterSave
    public void afterSave(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_UPDATE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getId(),
                organization.getName());
    }

    @HandleBeforeDelete
    public void beforeDelete(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_DELETE_ATTEMPT,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getId(),
                organization.getName());
    }

    @HandleAfterDelete
    public void afterDelete(Organization organization) {
        String currentMethodName = LogContext.currentMethodName();
        log.info(LogMessages.ORGANIZATION_DELETE_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                organization.getId(),
                organization.getName());
    }
}