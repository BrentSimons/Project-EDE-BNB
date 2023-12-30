import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {ErrorComponent} from "./error/error.component";
import {HomeComponent} from "./home/home.component";
import {ReservationPlannerComponent} from "./management_pages/reservation-planner/reservation-planner.component";
import {AuthGuard} from './auth.guard';
import {RoomComponent} from "./management_pages/room/room.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'error', component: ErrorComponent},
  {path: '', component: HomeComponent},
  {path: 'reservation_planner', component: ReservationPlannerComponent, canActivate: [AuthGuard]},
  {path: 'rooms', component: RoomComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
