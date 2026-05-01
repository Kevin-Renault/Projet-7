import { ComponentFixture, fakeAsync, flushMicrotasks, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';

import { OrganizationDetailsComponent } from './organization-details.component';
import { OrganizationService, Organization } from '../organization.service';
import { PersonService } from '../person.service';
import { MonitoringLoggerService } from '../monitoring-logger.service';

describe('OrganizationDetailsComponent', () => {
  let component: OrganizationDetailsComponent;
  let fixture: ComponentFixture<OrganizationDetailsComponent>;
  let organizationServiceSpy: jasmine.SpyObj<OrganizationService>;
  let personServiceSpy: jasmine.SpyObj<PersonService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let monitoringLoggerSpy: jasmine.SpyObj<MonitoringLoggerService>;
  let routeGetSpy: jasmine.Spy;

  const orion: Organization = {
    id: 7,
    name: 'Orion',
    createdAt: new Date(),
    updatedAt: new Date(),
    persons: []
  };

  beforeEach(async () => {
    routeGetSpy = jasmine.createSpy('get');
    organizationServiceSpy = jasmine.createSpyObj<OrganizationService>('OrganizationService', ['fetchById', 'save', 'deleteById']);
    personServiceSpy = jasmine.createSpyObj<PersonService>('PersonService', ['fetchById']);
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);
    monitoringLoggerSpy = jasmine.createSpyObj<MonitoringLoggerService>('MonitoringLoggerService', ['logError', 'logHttp']);

    await TestBed.configureTestingModule({
      imports: [OrganizationDetailsComponent],
      providers: [
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: routeGetSpy } } } },
        { provide: OrganizationService, useValue: organizationServiceSpy },
        { provide: PersonService, useValue: personServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: MonitoringLoggerService, useValue: monitoringLoggerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrganizationDetailsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark component as new when route param is new', () => {
    routeGetSpy.and.returnValue('new');

    component.ngOnInit();

    expect(component.isNew).toBeTrue();
    expect(organizationServiceSpy.fetchById).not.toHaveBeenCalled();
  });

  it('should load organization when route param is an id', fakeAsync(() => {
    routeGetSpy.and.returnValue('7');
    organizationServiceSpy.fetchById.and.returnValue(Promise.resolve(orion));

    component.ngOnInit();
    flushMicrotasks();

    expect(organizationServiceSpy.fetchById).toHaveBeenCalledWith(7);
    expect(component.org).toEqual(orion);
    expect(component.isNew).toBeFalse();
  }));

  it('should save and navigate when creating a new organization', fakeAsync(() => {
    component.isNew = true;
    component.org = { ...orion, id: undefined };
    organizationServiceSpy.save.and.returnValue(Promise.resolve(orion));

    component.saveOrg();
    flushMicrotasks();

    expect(organizationServiceSpy.save).toHaveBeenCalled();
    expect(component.org).toEqual(orion);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['organizations', 7]);
  }));

  it('should save without navigating when updating an existing organization', fakeAsync(() => {
    component.isNew = false;
    component.org = orion;
    organizationServiceSpy.save.and.returnValue(Promise.resolve(orion));

    component.saveOrg();
    flushMicrotasks();

    expect(organizationServiceSpy.save).toHaveBeenCalledWith(orion);
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  }));

  it('should log and stop delete when organization id is undefined', fakeAsync(() => {
    component.org = { ...orion, id: undefined };

    component.deleteOrg();
    flushMicrotasks();

    expect(monitoringLoggerSpy.logError).toHaveBeenCalledWith({ level: 'ERROR', message: 'deleteOrg called with undefined org id' });
    expect(organizationServiceSpy.deleteById).not.toHaveBeenCalled();
  }));

  it('should delete organization and navigate home', fakeAsync(() => {
    component.org = orion;
    organizationServiceSpy.deleteById.and.returnValue(Promise.resolve());

    component.deleteOrg();
    flushMicrotasks();

    expect(organizationServiceSpy.deleteById).toHaveBeenCalledWith(7);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['']);
  }));
});
