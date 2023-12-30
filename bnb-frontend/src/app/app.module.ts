import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './login/login.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {ErrorComponent} from './error/error.component';
import {NavbarComponent} from './navbar/navbar.component';
import {HomeComponent} from './home/home.component';
import {ReservationPlannerComponent} from './management_pages/reservation-planner/reservation-planner.component';
import {AppHttpModule} from './app-http.module';
import {HttpClientModule} from "@angular/common/http";
import { RoomComponent } from './management_pages/room/room.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    ErrorComponent,
    NavbarComponent,
    HomeComponent,
    ReservationPlannerComponent,
    RoomComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    AppHttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
