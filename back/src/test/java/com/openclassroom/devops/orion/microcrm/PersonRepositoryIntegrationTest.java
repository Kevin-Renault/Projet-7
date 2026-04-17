package com.openclassroom.devops.orion.microcrm;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PersonRepositoryIntegrationTest {

    private static final String EMAIL = "jdoe@example.net";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PHONE = "+1 (555) 123-4567";
    private static final String BIO = "Developer";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Person createJohnDoe() {
        Person person = new Person(FIRST_NAME, LAST_NAME, EMAIL);
        person.setPhone(PHONE);
        person.setBio(BIO);
        return person;
    }

    private void assertJohnDoe(Person person) {
        assertTrue(person.getId() > 0);
        assertNotNull(person.getCreatedAt());
        assertNotNull(person.getUpdatedAt());
        assertEquals(FIRST_NAME, person.getFirstName());
        assertEquals(LAST_NAME, person.getLastName());
        assertEquals(EMAIL, person.getEmail());
        assertEquals(PHONE, person.getPhone());
        assertEquals(BIO, person.getBio());
    }

    @Test
    void whenFindByEmail_thenReturnPerson() {
        // given
        Person person = createJohnDoe();
        personRepository.save(person);
        entityManager.flush();

        // when
        Optional<Person> found = personRepository.findByEmail(EMAIL);

        assertTrue(found.isPresent());
        assertJohnDoe(found.get());
    }

    @Test
    void createPerson_thenAddToOrganization_thenRemove() {
        // given
        Person person = createJohnDoe();
        personRepository.save(person);
        entityManager.flush();

        Organization organization = new Organization();
        organization.addPerson(person);
        entityManager.persist(organization);
        entityManager.flush();

        // when
        Optional<Person> persons = personRepository.findByEmail(EMAIL);

        assertTrue(persons.isPresent());
        assertJohnDoe(persons.get());

        Organization organizationfromDb = organizationRepository.findById(organization.getId()).get();
        organizationfromDb.getPersons().stream()
                .filter(p -> p.getEmail().equals(EMAIL))
                .findFirst()
                .ifPresent(p -> assertEquals(FIRST_NAME, p.getFirstName()));

        entityManager.clear();
        Person personFromDb = personRepository.findByEmail(EMAIL).orElseThrow();
        personRepository.delete(personFromDb);
        entityManager.flush();

        Organization reloadedOrganization = organizationRepository.findById(organization.getId()).orElseThrow();

        assertFalse(reloadedOrganization.getPersons().stream()
                .anyMatch(p -> p.getEmail().equals(EMAIL)));

    }

}