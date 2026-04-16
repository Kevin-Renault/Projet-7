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
    if (this.persons == null) {
      this.persons = new ArrayList<>();
    }
    this.persons.add(person);
    return this.persons;
  }

  public List<Person> removePerson(Person person) {
    if (this.persons == null) {
      this.persons = new ArrayList<>();
    }
    this.persons.remove(person);
    return this.persons;
  }

  public List<Person> getPersons() {
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
