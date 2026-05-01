package com.openclassroom.devops.orion.microcrm;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryOperationLoggingHandlerTest {

    private final RepositoryOperationLoggingHandler handler = new RepositoryOperationLoggingHandler();
    private final Logger logger = (Logger) LoggerFactory.getLogger(RepositoryOperationLoggingHandler.class);
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
    void shouldLogEntirePersonLifecycle() throws Exception {
        Person person = new Person("John", "Doe", "jdoe@example.net");
        setEntityId(person, 42L);

        handler.beforeCreate(person);
        handler.afterCreate(person);
        handler.beforeSave(person);
        handler.afterSave(person);
        handler.beforeDelete(person);
        handler.afterDelete(person);

        List<ILoggingEvent> events = appender.list;

        assertEquals(6, events.size());
        assertTrue(events.get(0).getFormattedMessage().contains(
                "RepositoryOperationLoggingHandler#beforeCreate create requested for firstName='John', lastName='Doe', email='jdoe@example.net'"));
        assertTrue(events.get(1).getFormattedMessage().contains("created person id=42 email='jdoe@example.net'"));
        assertTrue(events.get(2).getFormattedMessage()
                .contains("update requested for id=42 firstName='John', lastName='Doe', email='jdoe@example.net'"));
        assertTrue(events.get(3).getFormattedMessage().contains("updated person id=42 email='jdoe@example.net'"));
        assertTrue(events.get(4).getFormattedMessage().contains("delete requested for id=42 email='jdoe@example.net'"));
        assertTrue(events.get(5).getFormattedMessage().contains("deleted person id=42 email='jdoe@example.net'"));
    }

    @Test
    void shouldLogEntireOrganizationLifecycle() throws Exception {
        Organization organization = new Organization();
        organization.setName("Orion");
        setEntityId(organization, 7L);

        handler.beforeCreate(organization);
        handler.afterCreate(organization);
        handler.beforeSave(organization);
        handler.afterSave(organization);
        handler.beforeDelete(organization);
        handler.afterDelete(organization);

        List<ILoggingEvent> events = appender.list;

        assertEquals(6, events.size());
        assertTrue(events.get(0).getFormattedMessage()
                .contains("RepositoryOperationLoggingHandler#beforeCreate create requested for name='Orion'"));
        assertTrue(events.get(1).getFormattedMessage().contains("created organization id=7 name='Orion'"));
        assertTrue(events.get(2).getFormattedMessage().contains("update requested for id=7 name='Orion'"));
        assertTrue(events.get(3).getFormattedMessage().contains("updated organization id=7 name='Orion'"));
        assertTrue(events.get(4).getFormattedMessage().contains("delete requested for id=7 name='Orion'"));
        assertTrue(events.get(5).getFormattedMessage().contains("deleted organization id=7 name='Orion'"));
    }

    private void setEntityId(AuditableEntity entity, long id) throws Exception {
        Field idField = AuditableEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.setLong(entity, id);
    }
}