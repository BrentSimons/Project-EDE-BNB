import {ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import {AuthService} from "../auth.service";
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NavbarComponent implements OnInit {
  isAuthenticated = false;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.isSignedIn$.subscribe(isSignedIn => {
      // Do something with isSignedIn
      this.isAuthenticated = isSignedIn;
    });
  }
}
