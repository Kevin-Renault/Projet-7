/// <reference types="jasmine" />

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { fakeAsync, flushMicrotasks, TestBed } from '@angular/core/testing';

import { API_BASE_URL } from './config';
import { Organization } from './organization.service';
import { Person, PersonService } from './person.service';

describe('PersonService', () => {
  let service: PersonService;
  let httpMock: HttpTestingController;

  const johnDoe: Person = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'jdoe@example.net',
    phone: '+1 (555) 123-4567',
    bio: 'Developer',
    createdAt: new Date(),
    updatedAt: new Date(),
    organizations: []
  };

  const acme: Organization = {
    id: 7,
    name: 'Acme Corporation',
    createdAt: new Date(),
    updatedAt: new Date(),
    persons: []
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(PersonService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch a person by id and hydrate organizations', fakeAsync(() => {
    const promise = service.fetchById(1);
    let result: Person | undefined;
    promise.then(value => result = value);

    const personRequest = httpMock.expectOne(`${API_BASE_URL}/persons/1`);
    expect(personRequest.request.method).toBe('GET');
    personRequest.flush(johnDoe);

    flushMicrotasks();

    const organizationsRequest = httpMock.expectOne(`${API_BASE_URL}/persons/1/organizations`);
    expect(organizationsRequest.request.method).toBe('GET');
    organizationsRequest.flush({ _embedded: { organizations: [acme] } });

    flushMicrotasks();
    expect(result).toEqual({ ...johnDoe, organizations: [acme] });
  }));

  it('should fetch all persons', async () => {
    const promise = service.fetchAll();

    const request = httpMock.expectOne(`${API_BASE_URL}/persons`);
    expect(request.request.method).toBe('GET');
    request.flush({ _embedded: { persons: [johnDoe] } });

    await expectAsync(promise).toBeResolvedTo([johnDoe]);
  });

  it('should fetch organizations linked to a person', async () => {
    const promise = service.fetchPersonOrganizations(1);

    const request = httpMock.expectOne(`${API_BASE_URL}/persons/1/organizations`);
    expect(request.request.method).toBe('GET');
    request.flush({ _embedded: { organizations: [acme] } });

    await expectAsync(promise).toBeResolvedTo([acme]);
  });

  it('should delete a person by id', async () => {
    const promise = service.deleteById(1);

    const request = httpMock.expectOne(`${API_BASE_URL}/persons/1`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);

    await expectAsync(promise).toBeResolved();
  });

  it('should create a person and hydrate organizations', fakeAsync(() => {
    const person: Person = {
      id: undefined,
      firstName: 'John',
      lastName: 'Doe',
      email: 'jdoe@example.net',
      phone: '+1 (555) 123-4567',
      bio: 'Developer',
      createdAt: new Date(),
      updatedAt: undefined,
      organizations: []
    };

    const promise = service.save(person);
    let result: Person | undefined;
    promise.then(value => result = value);

    const createRequest = httpMock.expectOne(`${API_BASE_URL}/persons`);
    expect(createRequest.request.method).toBe('POST');
    createRequest.flush(johnDoe);

    flushMicrotasks();

    const organizationsRequest = httpMock.expectOne(`${API_BASE_URL}/persons/1/organizations`);
    expect(organizationsRequest.request.method).toBe('GET');
    organizationsRequest.flush({ _embedded: { organizations: [acme] } });

    flushMicrotasks();
    expect(result).toEqual({ ...johnDoe, organizations: [acme] });
  }));

  it('should update a person and hydrate organizations', fakeAsync(() => {
    const promise = service.save(johnDoe);
    let result: Person | undefined;
    promise.then(value => result = value);

    const updateRequest = httpMock.expectOne(`${API_BASE_URL}/persons/1`);
    expect(updateRequest.request.method).toBe('PUT');
    updateRequest.flush(johnDoe);

    flushMicrotasks();

    const organizationsRequest = httpMock.expectOne(`${API_BASE_URL}/persons/1/organizations`);
    expect(organizationsRequest.request.method).toBe('GET');
    organizationsRequest.flush({ _embedded: { organizations: [acme] } });

    flushMicrotasks();
    expect(result).toEqual({ ...johnDoe, organizations: [acme] });
  }));
});
