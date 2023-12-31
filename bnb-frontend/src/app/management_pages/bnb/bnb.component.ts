// bnb.component.ts
import { Component, OnInit } from '@angular/core';
import { Bnb } from '../../models/bnb.model';
import { BnbService } from '../../api_services/bnb.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-bnb',
  templateUrl: './bnb.component.html',
  styleUrls: ['./bnb.component.css'],
})
export class BnbComponent implements OnInit {
  bnbs: Bnb[] = [];
  newBnb: Bnb = {
    id: 0,
    name: '',
    roomCodes: [],
    city: '',
    postcode: '',
    address: '',
  };
  isUpdating: boolean = false;

  constructor(private bnbService: BnbService) {}

  ngOnInit(): void {
    this.loadBnbs();
  }

  loadBnbs(): void {
    this.bnbService.getAllBnbs().subscribe(
      (data) => {
        this.bnbs = data;
      },
      (error) => {
        console.error('Error fetching bnbs: ', error);
      }
    );
  }

  openCreateDialog(): void {
    const dialog = document.getElementById('createBnbDialog') as HTMLDialogElement;
    dialog.showModal();
  }

  closeCreateDialog(): void {
    const dialog = document.getElementById('createBnbDialog') as HTMLDialogElement;
    dialog.close();
  }

  updateOrCreateBnb(): void {
    if (this.isUpdating) {
      // Logic for updating bnb
      this.bnbService.updateBnb(this.newBnb.id, this.newBnb).subscribe(
        (data) => {
          console.log('Bnb updated successfully: ', data);
          this.loadBnbs();
        },
        (error) => {
          console.error('Error updating bnb: ', error);
        }
      );
    } else {
      // Logic for creating bnb
      this.bnbService.createBnb(this.newBnb).subscribe(
        (data) => {
          console.log('Bnb created successfully: ', data);
          this.loadBnbs();
        },
        (error) => {
          console.error('Error creating bnb: ', error);
        }
      );
    }

    // Reset newBnb and isUpdating
    this.newBnb = {
      id: 0,
      name: '',
      roomCodes: [],
      city: '',
      postcode: '',
      address: '',
    };
    this.isUpdating = false;
  }

  updateBnb(id: number): void {
    this.isUpdating = true;
    this.newBnb = <Bnb>{...this.bnbs.find((bnb) => bnb.id === id)};
    this.openCreateDialog(); // Open the dialog for updating
  }

  deleteBnb(id: number): void {
    this.bnbService.deleteBnb(id).subscribe(
      () => {
        console.log('Bnb deleted successfully');
        this.loadBnbs();
      },
      (error) => {
        console.error('Error deleting bnb: ', error);
      }
    );
  }
}
