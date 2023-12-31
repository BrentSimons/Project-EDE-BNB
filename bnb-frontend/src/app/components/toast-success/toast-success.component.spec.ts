import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastSuccessComponent } from './toast-success.component';

describe('ToastSuccesComponent', () => {
  let component: ToastSuccessComponent;
  let fixture: ComponentFixture<ToastSuccessComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ToastSuccessComponent]
    });
    fixture = TestBed.createComponent(ToastSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
