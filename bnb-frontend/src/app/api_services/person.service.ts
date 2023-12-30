import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { Person } from '../models/person.model';

@Injectable({
  providedIn: 'root',
})
export class PersonService {
  private apiUrl = 'http://localhost:7000/person/';
  auth = inject(AuthService);
  private headers = new HttpHeaders({
    Authorization: `Bearer ${this.auth.getJwtToken()}`,
  });

  constructor(private http: HttpClient) {}

  getAllPersons(): Observable<any> {
    return this.http.get(this.apiUrl + 'all', { headers: this.headers });
  }

  getPersonById(id: string): Observable<any> {
    return this.http.get(this.apiUrl + id, { headers: this.headers });
  }

  createPerson(person: Person): Observable<any> {
    return this.http.post(this.apiUrl, person, { headers: this.headers });
  }

  updatePerson(id: string, person: Person): Observable<any> {
    return this.http.put(this.apiUrl + id, person, { headers: this.headers });
  }

  deletePerson(id: string): Observable<any> {
    return this.http.delete(this.apiUrl + id, { headers: this.headers });
  }
}
