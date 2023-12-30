import {Router} from "@angular/router";

declare var google: any;
import {inject, Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private router = inject(Router);
  private _isSignedIn = new BehaviorSubject<boolean>(this.isSignedIn()); // Initialize with current state
  private _jwtToken = new BehaviorSubject<string | null>(this.getJwtToken()); // Initialize with current state
  public isSignedIn$ = this._isSignedIn.asObservable(); // Expose as Observable
  public jwtToken$ = this._jwtToken.asObservable(); // Expose JWT token as Observable

  constructor() { }

  signOut(){
    sessionStorage.removeItem("loggedInUser");
    sessionStorage.removeItem("jwtToken"); // Remove JWT token from sessionStorage
    google.accounts.id.disableAutoSelect();
    this.router.navigate(['/']);
    this._isSignedIn.next(false); // Update state
    this._jwtToken.next(null); // Update JWT token state
  }

  signIn(jwtToken: string) {
    sessionStorage.setItem("jwtToken", jwtToken); // Store JWT token in sessionStorage
    this._isSignedIn.next(true); // Update state
    this._jwtToken.next(jwtToken); // Update JWT token state
  }

  isSignedIn(): boolean {
    return (sessionStorage.getItem("loggedInUser") != null)
  }

  getJwtToken(): string | null {
    return sessionStorage.getItem("jwtToken");
  }
}
