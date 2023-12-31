import { Component, OnInit } from '@angular/core';
import { Person } from '../../models/person.model';
import { PersonService } from '../../api_services/person.service'; // Assuming the file name is person.service.ts
import { Observable } from 'rxjs';

@Component({
  selector: 'app-person',
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.css'],
})
export class PersonComponent implements OnInit {
  persons: any[] = [];
  newPerson: Person = {
    id: '',
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    accountNumber: 0,
    contact: { phoneNumber: '', emailAddress: '', address: '' },
  };
  isUpdating: boolean = false;

  constructor(private personService: PersonService) {}

  ngOnInit(): void {
    this.loadPersons();
  }

  loadPersons(): void {
    this.personService.getAllPersons().subscribe(
      (data) => {
        this.persons = data;
      },
      (error) => {
        console.error('Error fetching persons: ', error);
      }
    );
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

  updateOrCreatePerson(): void {
    if (this.isUpdating) {
      // Logic for updating person
      this.personService.updatePerson(this.newPerson.id, this.newPerson).subscribe(
        (data) => {
          console.log('Person updated successfully: ', data);
          this.loadPersons();
        },
        (error) => {
          console.error('Error updating person: ', error);
        }
      );
    } else {
      // Logic for creating person
      this.newPerson.id = this.generateUniqueId();
      this.personService.createPerson(this.newPerson).subscribe(
        (data) => {
          console.log('Person created successfully: ', data);
          this.loadPersons();
        },
        (error) => {
          console.error('Error creating person: ', error);
        }
      );
    }
    this.resetModal();
  }

  updatePerson(id: string): void {
    this.isUpdating = true;
    this.newPerson = { ...this.persons.find((person) => person.id === id) };
    this.openCreateDialog(); // Open the dialog for updating
  }

  deletePerson(id: string): void {
    this.personService.deletePerson(id).subscribe(
      () => {
        console.log('Person deleted successfully');
        this.loadPersons();
      },
      (error) => {
        console.error('Error deleting person: ', error);
      }
    );
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
    this.isUpdating = false;
  }
}
