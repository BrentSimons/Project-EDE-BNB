import {Component, OnInit} from '@angular/core';
import {Room} from "../../models/room.model";
import {Person} from "../../models/person.model";
import {RoomService} from "../../api_services/room.service";
import {PersonService} from "../../api_services/person.service";
import {ReservationService} from "../../api_services/reservation.service";
import {ReservationRequest} from "../../models/reservation.model";
import {NgForm} from "@angular/forms";
import {ToastSuccessService} from "../../components/toast-success/toast-success.service";

@Component({
  selector: 'app-reservation-planner',
  templateUrl: './reservation-planner.component.html',
  styleUrls: ['./reservation-planner.component.css']
})
export class ReservationPlannerComponent implements OnInit {
  rooms: Room[] = [];
  persons: Person[] = [];
  selectedRoom: Room | null = null;
  selectedPerson: Person | null = null;
  reservationRequest: ReservationRequest = {
    personId: '',
    roomCode: '',
    startDate: '',
    endDate: ''
  };

  constructor(private roomService: RoomService, private personService: PersonService, private reservationService: ReservationService, protected toastSuccessService: ToastSuccessService) {}

  ngOnInit(): void {
    this.fetchRooms();
    this.fetchPersons();
  }

  private fetchRooms(): void {
    this.roomService.getAllRooms().subscribe((rooms: Room[]) => {
      this.rooms = rooms;
    });
  }

  private fetchPersons(): void {
    this.personService.getAllPersons().subscribe((persons: Person[]) => {
      this.persons = persons;
    });
  }

  createNewReservation(): void {
    this.reservationService.createReservation(this.reservationRequest).subscribe(
      (response) => {
        this.showToast()
        // Reset the form or handle success as needed
      },
      (error) => {
        console.error('Error creating reservation', error);
        // Handle error as needed
      }
    );
  }

  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.createNewReservation();
    } else {
      // Handle form validation errors
    }
  }

  showToast(): void {
    this.toastSuccessService.show('Successfully created reservation!');
  }
}
