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
  public isSignedIn$ = this._isSignedIn.asObservable(); // Expose as Observable

  constructor() { }

  signOut(){
    sessionStorage.removeItem("loggedInUser");
    google.accounts.id.disableAutoSelect();
    this.router.navigate(['/'])
    this._isSignedIn.next(false); // Update state
  }

  signIn() {
    this._isSignedIn.next(true); // Update state
  }

  isSignedIn(): boolean {
    return (sessionStorage.getItem("loggedInUser") != null)
  }
}
