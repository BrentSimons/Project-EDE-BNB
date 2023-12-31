import {Component, Input} from '@angular/core';
import {ToastSuccessService} from "./toast-success.service";

@Component({
  selector: 'app-toast-succes',
  templateUrl: './toast-success.component.html',
  styleUrls: ['./toast-success.component.css']
})
export class ToastSuccessComponent {
  @Input() message: string = '';

  constructor(private toastSuccessService: ToastSuccessService) { }

  hide(): void {
    this.toastSuccessService.hide();
  }
}
