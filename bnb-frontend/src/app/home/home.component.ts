import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  activeTab: string = 'endpoint-1';  // Set the default active tab

  setActiveTab(tabId: string): void {
    this.activeTab = tabId;
  }
}
