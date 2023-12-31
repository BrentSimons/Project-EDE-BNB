import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ToastSuccessService {
  private toasts: string[] = [];

  show(message: string): void {
    this.toasts.push(message);
  }

  hide(): void {
    this.toasts.pop();
  }

  getToasts(): string[] {
    return this.toasts;
  }
}
