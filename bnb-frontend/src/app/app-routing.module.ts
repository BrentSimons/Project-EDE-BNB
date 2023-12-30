import {inject, NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {ErrorComponent} from "./error/error.component";
import {HomeComponent} from "./home/home.component";
import {ReservationPlannerComponent} from "./reservation-planner/reservation-planner.component";
import {AuthService} from "./auth.service";
import {AuthGuard} from './auth.guard';
const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'error', component: ErrorComponent},
  {path: '', component: HomeComponent},
  {path: 'reservations', component: ReservationPlannerComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
  isAuthenticated = false;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.isSignedIn$.subscribe(isSignedIn => {
      // Do something with isSignedIn
      this.isAuthenticated = isSignedIn;
    });
  }
}