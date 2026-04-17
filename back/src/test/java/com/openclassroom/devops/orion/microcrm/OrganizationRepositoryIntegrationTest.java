package com.openclassroom.devops.orion.microcrm;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class OrganizationRepositoryIntegrationTest {

    private static final String EMAIL = "jdoe@example.net";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PHONE = "+1 (555) 123-4567";
    private static final String BIO = "Developer";
    private static final String NAME = "Acme Corporation";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    // Crée un jeu de données réutilisable pour tester les relations avec une
    // personne valide.
    private Person createJohnDoe() {
        Person person = new Person(FIRST_NAME, LAST_NAME, EMAIL);
        person.setPhone(PHONE);
        person.setBio(BIO);
        return person;
    }

    // Vérifie à la fois les champs métier et les champs d'audit générés par JPA.
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

    // Prépare une organisation simple pour isoler le test sur le lien avec une
    // personne.
    private Organization createOrganization() {
        Organization organization = new Organization();
        organization.setName(NAME);
        return organization;
    }

    // Vérifie la persistance de l'organisation et ses métadonnées JPA.
    private void assertOrganization(Organization organization) {
        assertTrue(organization.getId() > 0);
        assertNotNull(organization.getCreatedAt());
        assertNotNull(organization.getUpdatedAt());
        assertEquals(NAME, organization.getName());
    }

    @Test
    void whenFindById_thenReturnOrganization() {
        // given: on persiste une organisation pour valider la lecture par identifiant.
        Organization organization = createOrganization();
        organizationRepository.save(organization);
        entityManager.flush();

        // when: on relit l'organisation depuis le repository.
        Optional<Organization> found = organizationRepository.findById(organization.getId());

        // then: on vérifie que l'entité est bien retrouvée avec les bonnes données.
        assertTrue(found.isPresent());
        assertOrganization(found.get());
    }

    @Test
    void createOrganization_thenAddPerson_thenRemove() {
        // given: on crée une organisation vide en base pour tester l'association avec
        // une personne.
        Organization organization = createOrganization();
        organizationRepository.save(organization);
        entityManager.flush();
        // given: on prépare une personne réutilisable pour la relation ManyToMany.
        Person person = createJohnDoe();
        personRepository.save(person);
        entityManager.flush();
        // On rattache la personne à l'organisation pour tester la persistance du lien.
        organization.addPerson(person);
        entityManager.flush();

        // when: on recharge l'organisation depuis la base pour vérifier ce qui a été
        // persisté.
        Organization organizationfromDb = organizationRepository.findById(organization.getId()).get();
        Optional<Person> personFromOrganization = organizationfromDb.getPersons().stream()
                .filter(p -> p.getEmail().equals(EMAIL))
                .findFirst();

        // then: le lien doit exister et la personne doit garder ses données d'origine.
        assertEquals(NAME, organizationfromDb.getName());
        assertTrue(personFromOrganization.isPresent());
        assertJohnDoe(personFromOrganization.get());

        // On retire ensuite la personne pour vérifier que la relation est bien
        // supprimée.
        organizationfromDb.removePerson(person);
        entityManager.flush();

        // La collection doit rester initialisée même après suppression du dernier
        // élément.
        assertNotNull(organizationfromDb.getPersons());
        assertEquals(0L, organizationfromDb.getPersons().stream()
                .filter(p -> p.getEmail().equals(EMAIL))
                .count());

        // Cas de sécurité pour vérifier que l'initialisation paresseuse de la
        // collection fonctionne.
        organizationfromDb.setPersons(null);
        entityManager.flush();
        assertNotNull(organizationfromDb.getPersons());
    }

}