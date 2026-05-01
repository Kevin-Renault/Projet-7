import { ComponentFixture, fakeAsync, flushMicrotasks, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';

import { PersonDetailsComponent } from './person-details.component';
import { PersonService, Person } from '../person.service';
import { Organization, OrganizationService } from '../organization.service';
import { MonitoringLoggerService } from '../monitoring-logger.service';

describe('PersonDetailsComponent', () => {
  let component: PersonDetailsComponent;
  let fixture: ComponentFixture<PersonDetailsComponent>;
  let personServiceSpy: jasmine.SpyObj<PersonService>;
  let organizationServiceSpy: jasmine.SpyObj<OrganizationService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let monitoringLoggerSpy: jasmine.SpyObj<MonitoringLoggerService>;
  let routeGetSpy: jasmine.Spy;

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

  beforeEach(async () => {
    johnDoe.organizations = [];
    acme.persons = [];

    routeGetSpy = jasmine.createSpy('get');
    personServiceSpy = jasmine.createSpyObj<PersonService>('PersonService', ['fetchById', 'save', 'deleteById']);
    organizationServiceSpy = jasmine.createSpyObj<OrganizationService>('OrganizationService', ['fetchAll', 'addPerson', 'removePerson']);
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);
    monitoringLoggerSpy = jasmine.createSpyObj<MonitoringLoggerService>('MonitoringLoggerService', ['logError', 'logHttp']);

    await TestBed.configureTestingModule({
      imports: [PersonDetailsComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: routeGetSpy } } } },
        { provide: PersonService, useValue: personServiceSpy },
        { provide: OrganizationService, useValue: organizationServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MonitoringLoggerService, useValue: monitoringLoggerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PersonDetailsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize organizations', fakeAsync(() => {
    organizationServiceSpy.fetchAll.and.returnValue(Promise.resolve([acme]));

    component.initialize();
    flushMicrotasks();

    expect(organizationServiceSpy.fetchAll).toHaveBeenCalled();
    expect(component.organizations).toEqual([acme]);
  }));

  it('should mark the component as new when route param is new', () => {
    routeGetSpy.and.returnValue('new');

    component.ngOnInit();

    expect(component.isNew).toBeTrue();
    expect(personServiceSpy.fetchById).not.toHaveBeenCalled();
  });

  it('should load a person when route param is an id', fakeAsync(() => {
    routeGetSpy.and.returnValue('1');
    personServiceSpy.fetchById.and.returnValue(Promise.resolve(johnDoe));

    component.ngOnInit();
    flushMicrotasks();

    expect(personServiceSpy.fetchById).toHaveBeenCalledWith(1);
    expect(component.person).toEqual(johnDoe);
    expect(component.isNew).toBeFalse();
  }));

  it('should save a person and navigate when creating', fakeAsync(() => {
    component.isNew = true;
    component.person = { ...johnDoe, id: undefined };
    personServiceSpy.save.and.returnValue(Promise.resolve(johnDoe));

    component.savePerson();
    flushMicrotasks();

    expect(personServiceSpy.save).toHaveBeenCalled();
    expect(component.person).toEqual(johnDoe);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['persons', 1]);
  }));

  it('should delete a person and navigate home', fakeAsync(() => {
    component.person = johnDoe;
    personServiceSpy.deleteById.and.returnValue(Promise.resolve());

    component.deletePerson();
    flushMicrotasks();

    expect(personServiceSpy.deleteById).toHaveBeenCalledWith(1);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['']);
  }));

  it('should add the selected organization locally without immediate persistence', fakeAsync(() => {
    component.person = johnDoe;
    component.selectedOrganization = acme;

    component.addSelectedOrganization();
    flushMicrotasks();

    expect(organizationServiceSpy.addPerson).not.toHaveBeenCalled();
    expect(personServiceSpy.fetchById).not.toHaveBeenCalled();
    expect(component.person.organizations).toEqual([acme]);
  }));

  it('should persist pending organization additions on save', fakeAsync(() => {
    component.person = { ...johnDoe, organizations: [] };
    component.selectedOrganization = acme;
    personServiceSpy.save.and.returnValue(Promise.resolve({ ...johnDoe, organizations: [] }));
    personServiceSpy.fetchById.and.returnValue(Promise.resolve({ ...johnDoe, organizations: [acme] }));

    component.addSelectedOrganization();
    component.savePerson();
    flushMicrotasks();

    expect(organizationServiceSpy.addPerson).toHaveBeenCalledWith(7, 1);
    expect(personServiceSpy.fetchById).toHaveBeenCalledWith(1);
    expect(component.person.organizations).toEqual([acme]);
  }));

  it('should remove an organization locally without immediate persistence', fakeAsync(() => {
    component.person = { ...johnDoe, organizations: [acme] };

    component.removeOrganization(acme);
    flushMicrotasks();

    expect(organizationServiceSpy.removePerson).not.toHaveBeenCalled();
    expect(personServiceSpy.fetchById).not.toHaveBeenCalled();
    expect(component.person.organizations).toEqual([]);
  }));

  it('should persist pending organization removals on save', fakeAsync(() => {
    component.person = { ...johnDoe, organizations: [acme] };
    personServiceSpy.save.and.returnValue(Promise.resolve({ ...johnDoe, organizations: [acme] }));
    personServiceSpy.fetchById.and.returnValue(Promise.resolve({ ...johnDoe, organizations: [] }));

    component.removeOrganization(acme);
    component.savePerson();
    flushMicrotasks();

    expect(organizationServiceSpy.removePerson).toHaveBeenCalledWith(7, 1);
    expect(personServiceSpy.fetchById).toHaveBeenCalledWith(1);
    expect(component.person.organizations).toEqual([]);
  }));

  it('should refresh the person from the repository', fakeAsync(() => {
    component.person = johnDoe;
    personServiceSpy.fetchById.and.returnValue(Promise.resolve({ ...johnDoe, firstName: 'Johnny' }));

    component.refresh();
    flushMicrotasks();

    expect(personServiceSpy.fetchById).toHaveBeenCalledWith(1);
    expect(component.person.firstName).toBe('Johnny');
    expect(component.isNew).toBeFalse();
  }));
});
