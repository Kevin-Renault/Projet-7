import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Person } from './person.service';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from './config';

@Injectable({ providedIn: 'root' })
export class OrganizationService {

  constructor(readonly client: HttpClient) {
  }

  async fetchById(id: number) {
    const org = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/organizations/${id}`)
    ) as Organization
    const persons = await this.fetchOrganizationPersons(org.id as number)
    org.persons = persons
    return org
  }

  async fetchAll() {
    const result = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/organizations`)
    ) as any
    return result["_embedded"].organizations as Organization[]
  }

  async fetchOrganizationPersons(id: number) {
    const result = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/organizations/${id}/persons`)
    ) as any
    const persons = result["_embedded"].persons as Person[]
    return persons
  }

  async deleteById(id: number) {
    await firstValueFrom(
      this.client.delete(`${API_BASE_URL}/organizations/${id}`)
    )
  }

  async save(org: Organization) {
    let response;
    if (org.id === undefined) {
      response = this.client.post(`${API_BASE_URL}/organizations`, {
        name: org.name
      })
    } else {
      response = this.client.put(`${API_BASE_URL}/organizations/${org.id}`, {
        name: org.name
      })
    }

    org = await firstValueFrom(response) as Organization

    const persons = await this.fetchOrganizationPersons(org.id as number)
    org.persons = persons

    return org
  }

  async addPerson(orgId: number, personId: number) {
    await firstValueFrom(
      this.client.put(
        `${API_BASE_URL}/organizations/${orgId}/persons`,
        `${API_BASE_URL}/persons/${personId}`,
        { headers: { 'Content-Type': 'text/uri-list' } }
      )
    )
  }

  async removePerson(orgId: number, personId: number) {
    await firstValueFrom(
      this.client.delete(
        `${API_BASE_URL}/persons/${personId}/organizations/${orgId}`
      )
    )
  }

}

export interface Organization {
  id?: number
  name: string
  createdAt: Date
  updatedAt?: Date

  persons: Person[]
}