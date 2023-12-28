import {Router} from "@angular/router";

declare var google: any;
import {inject, Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private router = inject(Router);

  constructor() { }

  signOut(){
    sessionStorage.removeItem("loggedInUser");
    google.accounts.id.disableAutoSelect();
    this.router.navigate(['/'])
  }

  isSignedIn(): boolean {
    return (sessionStorage.getItem("loggedInUser") != null)
  }
}
