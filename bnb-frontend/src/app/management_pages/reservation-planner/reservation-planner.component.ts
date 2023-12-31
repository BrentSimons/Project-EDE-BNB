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

  newPerson: Person = {
    id: '',
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    accountNumber: 0,
    contact: { phoneNumber: '', emailAddress: '', address: '' },
  };

  constructor(private roomService: RoomService, private personService: PersonService, private reservationService: ReservationService, protected toastSuccessService: ToastSuccessService) {
  }

  ngOnInit(): void {
    this.fetchRooms();
    this.fetchPersons();
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
      this.reservationRequest = {
        personId: '',
        roomCode: '',
        startDate: '',
        endDate: ''
      };
    }
  }

  showToast(): void {
    this.toastSuccessService.show('Successfully created reservation!');
  }

  openCreateDialog(): void {
    const dialog = document.getElementById(
      'createPersonDialog'
    ) as HTMLDialogElement;
    dialog.showModal();
  }

  closeCreateDialog(): void {
    const dialog = document.getElementById(
      'createPersonDialog'
    ) as HTMLDialogElement;
    dialog.close();
    this.resetModal();
  }

  CreatePerson(): void {
    // Logic for creating person
    this.newPerson.id = this.generateUniqueId();
    this.personService.createPerson(this.newPerson).subscribe(
      (data) => {
        console.log('Person created successfully: ', data);
        this.fetchPersons();
      },
      (error) => {
        console.error('Error creating person: ', error);
      }
    );
    this.resetModal();
  }

  generateUniqueId(): string {
    const generateRandomString = (length: number): string =>
      [...Array(length)].map(() => Math.random().toString(36)[2]).join('');

    const padZero = (num: number): string => (num < 10 ? `0${num}` : `${num}`);

    const currentDate = new Date();
    const newId: number = this.persons.length + 1;
    const result: string = `person_${newId}${generateRandomString(3)}${padZero(currentDate.getDate())}${padZero(currentDate.getHours())}`;
    return result;
  }

  private resetModal() {
    // Reset newPerson and isUpdating
    this.newPerson = {
      id: '',
      firstName: '',
      lastName: '',
      dateOfBirth: '',
      accountNumber: 0,
      contact: { phoneNumber: '', emailAddress: '', address: '' },
    };
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
}
