package com.openclassroom.devops.orion.microcrm;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ro.polak.springboot.datafixtures.DataFixture;
import ro.polak.springboot.datafixtures.DataFixtureSet;

@Component
public class InitialDataFixture implements DataFixture {

    private static final Logger log = LoggerFactory.getLogger(InitialDataFixture.class);

    private PersonRepository personRepository;

    private OrganizationRepository organizationRepository;

    public InitialDataFixture(PersonRepository personRepository,
            OrganizationRepository organizationRepository) {
        this.personRepository = personRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public boolean canBeLoaded() {
        String currentMethodName = LogContext.currentMethodName();
        long personCount = personRepository.count();
        boolean canLoad = personCount == 0;
        log.info(LogMessages.FIXTURE_CHECK, getClass().getSimpleName(), currentMethodName, personCount, canLoad);
        return canLoad;
    }

    @Override
    public DataFixtureSet getSet() {
        return DataFixtureSet.DICTIONARY;
    }

    @Override
    public void load() {
        String currentMethodName = LogContext.currentMethodName();

        Person jdoe = new Person("John", "Doe", "jdoe@example.net");

        Organization orionInc = new Organization();
        orionInc.setName("Orion Incorporated");
        orionInc.addPerson(jdoe);

        log.info(LogMessages.FIXTURE_LOAD_START,
                getClass().getSimpleName(),
                currentMethodName,
                orionInc.getName(),
                jdoe.getEmail());

        organizationRepository.saveAll(Arrays.asList(orionInc));

        log.info(LogMessages.FIXTURE_LOAD_DONE,
                getClass().getSimpleName(),
                currentMethodName,
                orionInc.getName(),
                orionInc.getPersons().size());
    }

}