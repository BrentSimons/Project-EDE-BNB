// reservation.component.ts
import { Component, OnInit } from '@angular/core';
import { Reservation, ReservationRequest } from '../../models/reservation.model';
import { ReservationService } from '../../api_services/reservation.service';
import { Observable } from 'rxjs';
import {Person} from "../../models/person.model";
import {PersonService} from "../../api_services/person.service";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css'],
})
export class ReservationComponent implements OnInit {
  reservations: Reservation[] = [];
  persons: Person[] = [];
  newReservation: ReservationRequest = {
    personId: '',
    roomCode: '',
    startDate: '',
    endDate: '',
  };
  isUpdating: boolean = false;

  constructor(private reservationService: ReservationService, private personService: PersonService) {}

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.reservationService.getAllReservations().subscribe(
      (data) => {
        this.reservations = data;
      },
      (error) => {
        console.error('Error fetching reservations: ', error);
      }
    );

    this.personService.getAllPersons().subscribe(
      (data) => {
        this.persons = data;
      },
      (error) => {
        console.error('Error fetching persons', error);
      }
    );
  }

  openCreateDialog(): void {
    const dialog = document.getElementById('createReservationDialog') as HTMLDialogElement;
    dialog.showModal();
  }

  closeCreateDialog(): void {
    const dialog = document.getElementById('createReservationDialog') as HTMLDialogElement;
    dialog.close();
  }

  updateOrCreateReservation(): void {
    if (this.isUpdating) {
      // Logic for updating reservation
      if (this.newReservation.id !== undefined) {
        this.reservationService.updateReservation(this.newReservation.id, this.newReservation).subscribe(
          (data) => {
            console.log('Reservation updated successfully: ', data);
            this.loadReservations();
          },
          (error) => {
            console.error('Error updating reservation: ', error);
          }
        );
      }
    } else {
      // Logic for creating reservation
      this.reservationService.createReservation(this.newReservation).subscribe(
        (data) => {
          console.log('Reservation created successfully: ', data);
          this.loadReservations();
        },
        (error) => {
          console.error('Error creating reservation: ', error);
        }
      );
    }

    // Reset newReservation and isUpdating
    this.newReservation = {
      personId: '',
      roomCode: '',
      startDate: '',
      endDate: '',
    };
    this.isUpdating = false;
  }

  updateReservation(id: number): void {
    this.isUpdating = true;
    const foundReservation = this.reservations.find((reservation) => reservation.id === id);
    if (foundReservation !== undefined) {
      this.newReservation = { ...foundReservation };
      this.openCreateDialog(); // Open the dialog for updating
    }
  }

  deleteReservation(id: number): void {
    this.reservationService.deleteReservation(id).subscribe(
      () => {
        console.log('Reservation deleted successfully');
        this.loadReservations();
      },
      (error) => {
        console.error('Error deleting reservation: ', error);
      }
    );
  }

  getPersonName(personId: string): string {
    const person = this.persons.find(person => person.id === personId);
    return person ? `${person.firstName} ${person.lastName}` : '';
  }

}
