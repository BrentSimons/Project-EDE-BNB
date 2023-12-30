import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from "../models/room.model";
import {AuthService} from "../auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private apiUrl = 'http://localhost:7000/room/';
  auth = inject(AuthService);
  private headers = new HttpHeaders({
    'Authorization': `Bearer ${this.auth.getJwtToken()}`,
  });

  constructor(private http: HttpClient) {
  }

  getAllRooms(): Observable<any> {
    return this.http.get(this.apiUrl + 'all', { headers: this.headers });
  }

  getRoomById(id: number): Observable<any> {
    return this.http.get(this.apiUrl + id, { headers: this.headers });
  }

  createRoom(room: any): Observable<any> {
    return this.http.post(this.apiUrl, room, { headers: this.headers });
  }

  updateRoom(id: number, room: any): Observable<any> {
    return this.http.put(this.apiUrl + id, room, { headers: this.headers });
  }

  deleteRoom(id: number): Observable<any> {
    return this.http.delete(this.apiUrl + id, { headers: this.headers });
  }
}
