// bnb.component.ts
import { Component, OnInit } from '@angular/core';
import { Bnb } from '../../models/bnb.model';
import { BnbService } from '../../api_services/bnb.service';
import { Observable } from 'rxjs';
import {RoomService} from "../../api_services/room.service";
import {Room} from "../../models/room.model";

@Component({
  selector: 'app-bnb',
  templateUrl: './bnb.component.html',
  styleUrls: ['./bnb.component.css'],
})
export class BnbComponent implements OnInit {
  bnbs: Bnb[] = [];
  rooms: Room[] = [];
  newBnb: Bnb = {
    id: 0,
    name: '',
    roomCodes: [],
    city: '',
    postcode: '',
    address: '',
  };
  newRoom: any = { roomCode: '', name: '', size: 0 };
  isUpdating: boolean = false;
  private selectedBnbId: number | undefined;
  private newRoomCreate: { roomRequest: Room | undefined; bnbId: number | undefined; } | undefined;

  constructor(private bnbService: BnbService, private roomService: RoomService) {}

  ngOnInit(): void {
    this.loadBnbs();
    this.loadRooms();
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

  loadRooms(): void {
    this.roomService.getAllRooms().subscribe(
      (data) => {
        this.rooms = data;
      },
      (error) => {
        console.error('Error fetching rooms: ', error);
      }
    );
  }

  openRoomCreateDialog(bnbid: number): void {
    const dialog = document.getElementById('createRoomDialog') as HTMLDialogElement;
    dialog.showModal();
    this.selectedBnbId = bnbid;
  }

  closeRoomCreateDialog(): void {
    const dialog = document.getElementById('createRoomDialog') as HTMLDialogElement;
    this.resetRoomModal();
    dialog.close();
  }
  CreateRoom(): void {
    // Logic for creating room
    this.newRoomCreate = {
      roomRequest: this.newRoom,
      bnbId: this.selectedBnbId
    }
    this.roomService.createRoom(this.newRoomCreate).subscribe(
      (data) => {
        console.log('Room created successfully: ', data);
        this.loadBnbs();
      },
      (error) => {
        console.error('Error creating room: ', error);
      }
    );
    this.resetRoomModal();
  }

  resetRoomModal() {
    // Reset newRoom and isUpdating
    this.newRoom = { roomCode: '', name: '', size: 0 };
    this.selectedBnbId = 0;
  }
}
