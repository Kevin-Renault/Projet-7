import { NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Person, PersonService } from '../person.service';
import { Organization, OrganizationService } from '../organization.service';

@Component({
  selector: 'app-person-details',
  standalone: true,
  imports: [NgIf, FormsModule, NgFor, RouterLink],
  templateUrl: './person-details.component.html'
})
export class PersonDetailsComponent implements OnInit {
  person: Person = {
    id: undefined as (number | undefined),
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
    bio: '',
    createdAt: new Date(),
    updatedAt: undefined as (Date | undefined),
    organizations: [] as (Organization[])
  };

  organizations: Organization[] = []
  selectedOrganization: Organization | null = null;
  isNew: boolean = false;

  constructor(readonly route: ActivatedRoute, readonly personService: PersonService, readonly organizationService: OrganizationService, readonly router: Router) {
  }

  async initialize() {
    this.organizations = await this.organizationService.fetchAll()
  }

  ngOnInit(): void {
    void this.initialize()

    this.route.paramMap.subscribe(routeParams => {
      const personIdParam = routeParams.get('personId');

      if (personIdParam === 'new') {
        this.isNew = true
        this.person = {
          id: undefined,
          firstName: '',
          lastName: '',
          phone: '',
          email: '',
          bio: '',
          createdAt: new Date(),
          updatedAt: undefined,
          organizations: []
        }
        return
      }

      if (typeof personIdParam === 'string') {
        const personId = Number.parseInt(personIdParam, 10)
        if (Number.isFinite(personId)) {
          this.personService.fetchById(personId).then(p => {
            this.person = p
            this.isNew = false
          })
        }
      }
    })
  }

  async savePerson() {
    const pendingOrganizationIds = this.isNew
      ? this.person.organizations
        .map(org => org.id)
        .filter((id): id is number => id !== undefined)
      : []

    const savedPerson = await this.personService.save({
      ...this.person
    })

    this.person = savedPerson

    if (pendingOrganizationIds.length > 0 && this.person.id !== undefined) {
      await Promise.all(
        pendingOrganizationIds.map(orgId => this.organizationService.addPerson(orgId, this.person.id as number))
      )
      await this.refresh()
    }

    if (this.isNew) {
      this.router.navigate(["persons", this.person.id])
    }
  }

  deletePerson() {
    if (this.person.id === undefined) return
    this.personService.deleteById(this.person.id).then(() => {
      this.router.navigate([""])
    })
  }

  async addSelectedOrganization() {
    if (this.selectedOrganization?.id === undefined) return

    const alreadyLinked = this.person.organizations.some(org => org.id === this.selectedOrganization?.id)
    if (alreadyLinked) return

    if (this.person.id === undefined) {
      this.person.organizations = [...this.person.organizations, this.selectedOrganization]
      this.selectedOrganization = null
      return
    }

    await this.organizationService.addPerson(this.selectedOrganization.id, this.person.id)
    this.selectedOrganization = null
    await this.refresh()
  }

  async removeOrganization(org: Organization) {
    if (org?.id === undefined) return

    if (this.person.id === undefined) {
      this.person.organizations = this.person.organizations.filter(linkedOrg => linkedOrg.id !== org.id)
      return
    }

    await this.organizationService.removePerson(org.id, this.person.id)
    await this.refresh()
  }

  async refresh() {
    if (this.person.id === undefined) return
    this.person = await this.personService.fetchById(this.person.id)
    this.isNew = false
  }
}
