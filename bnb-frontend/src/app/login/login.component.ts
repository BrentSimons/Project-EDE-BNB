import {Router} from "@angular/router";

declare var google: any;
import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  private router = inject(Router);

  ngOnInit(): void {
    google.accounts.id.initialize({
      client_id: '965800561312-90pljktekk5ftj97q1snv1j749avv1ri.apps.googleusercontent.com',
      callback: (resp: any)=>{ this.handleLogin(resp) }
    });
  google.accounts.id.renderButton(document.getElementById("google-btn"), {
    theme: 'filled_blue',
    size: 'large',
    shape: 'rectangle',
    width: '350'
  })
  }

  private decodeToken(token: string) {
    return JSON.parse(atob(token.split(".")[1]))
  }

  handleLogin(response: any) {
    if (response) {
      //console.log(response)
      // decode the token
      const payLoad = this.decodeToken(response.credential);
      // store in session
      // console.log(payLoad)
      sessionStorage.setItem("loggedInUser", JSON.stringify(payLoad));
      // navigate to other page
      this.router.navigate(['dashboard'])
    }
  }
}