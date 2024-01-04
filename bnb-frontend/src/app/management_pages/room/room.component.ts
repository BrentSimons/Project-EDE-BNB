import {Component, OnInit} from '@angular/core';
import {Room} from "../../models/room.model";
import {RoomService} from "../../api_services/room.service";
import {Observable} from "rxjs";
import {Bnb} from "../../models/bnb.model";
import {BnbService} from "../../api_services/bnb.service";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent implements OnInit {
  bnbs: Bnb[] = [];
  rooms: any[] = [];
  newRoom: any = { roomCode: '', name: '', size: 0 };
  isUpdating: boolean = false;

  constructor(private roomService: RoomService, private bnbService: BnbService) { }

  ngOnInit(): void {
    this.loadRooms();
    this.loadBnbs();
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
    const dialog = document.getElementById('createRoomDialog') as HTMLDialogElement;
    dialog.showModal();
  }

  closeCreateDialog(): void {
    const dialog = document.getElementById('createRoomDialog') as HTMLDialogElement;
    this.resetModal()
    dialog.close();
  }
  updateOrCreateRoom(): void {
    if (this.isUpdating) {
      // Logic for updating room
      this.roomService.updateRoom(this.newRoom.id, this.newRoom).subscribe(
        (data) => {
          console.log('Room updated successfully: ', data);
          this.loadRooms();
        },
        (error) => {
          console.error('Error updating room: ', error);
        }
      );
    } else {
      // Logic for creating room
      this.roomService.createRoom(this.newRoom).subscribe(
        (data) => {
          console.log('Room created successfully: ', data);
          this.loadRooms();
        },
        (error) => {
          console.error('Error creating room: ', error);
        }
      );
    }
    this.resetModal();
  }

  updateRoom(id: number): void {
    this.isUpdating = true;
    this.newRoom = { ...this.rooms.find(room => room.id === id) };
    this.openCreateDialog(); // Open the dialog for updating
  }

  deleteRoom(id: number): void {
    // Fetch bnbId associated with the room
    const roomToDelete = this.rooms.find(room => room.id === id);
    const bnbId = this.bnbs.find(bnb => bnb.roomCodes.includes(roomToDelete.roomCode))?.id;

    // Call deleteRoom with roomId and bnbId
    if (bnbId) {
      this.roomService.deleteRoom(id, bnbId).subscribe(
        () => {
          console.log('Room deleted successfully');
          this.loadRooms();
        },
        (error) => {
          console.error('Error deleting room: ', error);
        }
      );
    }
  }

  resetModal() {
    // Reset newRoom and isUpdating
    this.newRoom = { roomCode: '', name: '', size: 0 };
    this.isUpdating = false;
  }
}
