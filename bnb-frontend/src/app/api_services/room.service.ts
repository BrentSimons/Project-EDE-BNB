import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Room} from "../models/room.model";
import {AuthService} from "../auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  private apiUrl = 'http://localhost:7000/room/all';
  auth = inject(AuthService);

  constructor(private http: HttpClient) {
  }

  getRooms(): Observable<any> {
    const token = this.auth.getJwtToken();
    // Make sure there's a token before making the request
    if (token) {
      // Set the Authorization header
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`,
      });
      // Make the GET request
      return this.http.get<Room[]>(this.apiUrl, {headers: headers});
    } else {
      // Handle the case where no token is found
      console.error('No token found');
      // @ts-ignore
      return null; // Or return an Observable with an appropriate error state
    }
  }
}
