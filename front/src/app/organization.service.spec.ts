import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { fakeAsync, flushMicrotasks, TestBed } from '@angular/core/testing';

import { API_BASE_URL } from './config';
import { Organization, OrganizationService } from './organization.service';
import { Person } from './person.service';

describe('OrganizationService', () => {
  let service: OrganizationService;
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
    service = TestBed.inject(OrganizationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch an organization by id and hydrate persons', fakeAsync(() => {
    const promise = service.fetchById(7);

    const orgRequest = httpMock.expectOne(`${API_BASE_URL}/organizations/7`);
    expect(orgRequest.request.method).toBe('GET');
    orgRequest.flush(acme);

    flushMicrotasks();

    const personsRequest = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons`);
    expect(personsRequest.request.method).toBe('GET');
    personsRequest.flush({ _embedded: { persons: [johnDoe] } });

    flushMicrotasks();
    expectAsync(promise).toBeResolvedTo({ ...acme, persons: [johnDoe] });
  }));

  it('should fetch all organizations', async () => {
    const promise = service.fetchAll();

    const request = httpMock.expectOne(`${API_BASE_URL}/organizations`);
    expect(request.request.method).toBe('GET');
    request.flush({ _embedded: { organizations: [acme] } });

    await expectAsync(promise).toBeResolvedTo([acme]);
  });

  it('should fetch persons linked to an organization', async () => {
    const promise = service.fetchOrganizationPersons(7);

    const request = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons`);
    expect(request.request.method).toBe('GET');
    request.flush({ _embedded: { persons: [johnDoe] } });

    await expectAsync(promise).toBeResolvedTo([johnDoe]);
  });

  it('should delete an organization by id', async () => {
    const promise = service.deleteById(7);

    const request = httpMock.expectOne(`${API_BASE_URL}/organizations/7`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);

    await expectAsync(promise).toBeResolved();
  });

  it('should create an organization and hydrate persons', fakeAsync(() => {
    const organization: Organization = {
      id: undefined,
      name: 'Acme Corporation',
      createdAt: new Date(),
      updatedAt: undefined,
      persons: []
    };

    const promise = service.save(organization);

    const createRequest = httpMock.expectOne(`${API_BASE_URL}/organizations`);
    expect(createRequest.request.method).toBe('POST');
    createRequest.flush(acme);

    flushMicrotasks();

    const personsRequest = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons`);
    expect(personsRequest.request.method).toBe('GET');
    personsRequest.flush({ _embedded: { persons: [johnDoe] } });

    flushMicrotasks();
    expectAsync(promise).toBeResolvedTo({ ...acme, persons: [johnDoe] });
  }));

  it('should update an organization and hydrate persons', fakeAsync(() => {
    const promise = service.save(acme);

    const updateRequest = httpMock.expectOne(`${API_BASE_URL}/organizations/7`);
    expect(updateRequest.request.method).toBe('PUT');
    updateRequest.flush(acme);

    flushMicrotasks();

    const personsRequest = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons`);
    expect(personsRequest.request.method).toBe('GET');
    personsRequest.flush({ _embedded: { persons: [johnDoe] } });

    flushMicrotasks();
    expectAsync(promise).toBeResolvedTo({ ...acme, persons: [johnDoe] });
  }));

  it('should add a person to an organization', async () => {
    const promise = service.addPerson(7, 1);

    const request = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons`);
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toBe(`${API_BASE_URL}/persons/1`);
    expect(request.request.headers.get('Content-Type')).toBe('text/uri-list');
    request.flush(null);

    await expectAsync(promise).toBeResolved();
  });

  it('should remove a person from an organization', async () => {
    const promise = service.removePerson(7, 1);

    const request = httpMock.expectOne(`${API_BASE_URL}/organizations/7/persons/1`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null);

    await expectAsync(promise).toBeResolved();
  });
});
