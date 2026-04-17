package com.openclassroom.devops.orion.microcrm;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Organization extends AuditableEntity {

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Person> persons;

  public List<Person> addPerson(Person person) {
    getPersons().add(person);
    return getPersons();
  }

  public List<Person> removePerson(Person person) {
    getPersons().remove(person);
    return getPersons();
  }

  public List<Person> getPersons() {
    if (this.persons == null) {
      this.persons = new ArrayList<>();
    }
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
