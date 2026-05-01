import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Organization } from './organization.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { API_BASE_URL } from './config'

@Injectable({ providedIn: 'root' })
export class PersonService {
  constructor(readonly client: HttpClient) {
  }

  async fetchById(id: number) {
    const person = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/persons/${id}`)
    ) as Person
    const organizations = await this.fetchPersonOrganizations(person.id as number)
    person.organizations = organizations
    return person
  }

  async fetchAll() {
    const result = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/persons`)
    ) as any
    const persons = result["_embedded"].persons as Person[]
    return persons
  }

  async fetchPersonOrganizations(id: number) {
    const result = await firstValueFrom(
      this.client.get(`${API_BASE_URL}/persons/${id}/organizations`)
    ) as any
    const organizations = result["_embedded"].organizations as Organization[]
    return organizations
  }

  async deleteById(id: number) {
    await firstValueFrom(
      this.client.delete(`${API_BASE_URL}/persons/${id}`)
    )
  }

  async save(person: Person) {
    const isNew = person.id === undefined
    let response: HttpResponse<any>;
    if (person.id === undefined) {
      response = await firstValueFrom(this.client.post(`${API_BASE_URL}/persons`, {
        firstName: person.firstName,
        lastName: person.lastName,
        bio: person.bio,
        phone: person.phone,
        email: person.email,
      }, { observe: 'response' }))
    } else {
      response = await firstValueFrom(this.client.put(`${API_BASE_URL}/persons/${person.id}`, {
        firstName: person.firstName,
        lastName: person.lastName,
        bio: person.bio,
        phone: person.phone,
        email: person.email,
      }, { observe: 'response' }))
    }

    const body = response.body as Partial<Person> | null
    const locationHeader = response.headers.get('Location')

    if (body?.id !== undefined) {
      person = await this.fetchById(body.id)
    } else if (locationHeader) {
      const personIdFromLocation = Number.parseInt(locationHeader.split('/').pop() ?? '', 10)
      if (Number.isFinite(personIdFromLocation)) {
        person = await this.fetchById(personIdFromLocation)
      }
    } else if (!isNew && person.id !== undefined) {
      person = await this.fetchById(person.id)
    }

    if (person.id === undefined) {
      throw new Error('Unable to resolve saved person id from API response')
    }

    const organizations = await this.fetchPersonOrganizations(person.id)
    person.organizations = organizations

    return person
  }

}


export interface Person {
  id?: number
  firstName: string
  lastName: string
  email: string
  phone: string
  bio: string
  createdAt: Date
  updatedAt?: Date
  organizations: Organization[];
}
