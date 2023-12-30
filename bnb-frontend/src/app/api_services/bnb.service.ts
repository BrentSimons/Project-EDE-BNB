// bnb.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { Bnb } from '../models/bnb.model'; // Assuming the file name is bnb.model.ts

@Injectable({
  providedIn: 'root',
})
export class BnbService {
  private apiUrl = 'http://localhost:7000/bnb/';
  auth = inject(AuthService);
  private headers = new HttpHeaders({
    Authorization: `Bearer ${this.auth.getJwtToken()}`,
  });

  constructor(private http: HttpClient) {}

  getAllBnbs(): Observable<any> {
    return this.http.get(this.apiUrl + 'all', { headers: this.headers });
  }

  getBnbByName(name: string): Observable<any> {
    return this.http.get(this.apiUrl + name, { headers: this.headers });
  }

  createBnb(bnb: {}): Observable<any> {
    return this.http.post(this.apiUrl, bnb, { headers: this.headers });
  }

  updateBnb(name: string, bnb: {}): Observable<any> {
    return this.http.put(this.apiUrl + name, bnb, { headers: this.headers });
  }

  deleteBnb(name: string): Observable<any> {
    return this.http.delete(this.apiUrl + name, { headers: this.headers });
  }
}
