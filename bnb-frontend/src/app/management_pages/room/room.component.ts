import {Component, OnInit} from '@angular/core';
import {Room} from "../../models/room.model";
import {RoomService} from "../../api_services/room.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent implements OnInit {
  rooms: any[] = [];
  newRoom: any = { roomCode: '', name: '', size: 0 };
  isUpdating: boolean = false;

  constructor(private roomService: RoomService) { }

  ngOnInit(): void {
    this.loadRooms();
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
    this.roomService.deleteRoom(id).subscribe(
      () => {
        console.log('Room deleted successfully');
        this.loadRooms();
      },
      (error) => {
        console.error('Error deleting room: ', error);
      }
    );
  }

  resetModal() {
    // Reset newRoom and isUpdating
    this.newRoom = { roomCode: '', name: '', size: 0 };
    this.isUpdating = false;
  }
}
