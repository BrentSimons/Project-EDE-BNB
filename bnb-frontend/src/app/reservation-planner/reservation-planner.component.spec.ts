import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationPlannerComponent } from './reservation-planner.component';

describe('ReservationPlannerComponent', () => {
  let component: ReservationPlannerComponent;
  let fixture: ComponentFixture<ReservationPlannerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReservationPlannerComponent]
    });
    fixture = TestBed.createComponent(ReservationPlannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
