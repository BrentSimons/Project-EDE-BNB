// reservation.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth.service';
import { Reservation, ReservationRequest } from '../models/reservation.model'; // Assuming the file name is reservation.model.ts

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private apiUrl = 'http://localhost:7000/reservation/';
  auth = inject(AuthService);
  private headers = new HttpHeaders({
    Authorization: `Bearer ${this.auth.getJwtToken()}`,
  });

  constructor(private http: HttpClient) {}

  getAllReservations(): Observable<any> {
    return this.http.get(this.apiUrl + 'all', { headers: this.headers });
  }

  getReservationById(id: number): Observable<any> {
    return this.http.get(this.apiUrl + id, { headers: this.headers });
  }

  createReservation(reservationRequest: ReservationRequest): Observable<any> {
    return this.http.post(this.apiUrl, reservationRequest, { headers: this.headers });
  }

  updateReservation(id: number, reservationRequest: ReservationRequest): Observable<any> {
    return this.http.put(this.apiUrl + id, reservationRequest, { headers: this.headers });
  }

  deleteReservation(id: number): Observable<any> {
    return this.http.delete(this.apiUrl + id, { headers: this.headers });
  }
}
