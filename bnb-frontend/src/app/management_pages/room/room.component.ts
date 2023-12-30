import {Component} from '@angular/core';
import {Room} from "../../models/room.model";
import {RoomService} from "../../api_services/room.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent {
  public rooms$: Observable<Room[]> | undefined;

  constructor(private roomService: RoomService) { }

  ngOnInit(): void {
    this.rooms$ = this.roomService.getRooms();
  }

  trackRoomById(room: any): string {
    return room.id; // Replace 'id' with the actual property representing the unique identifier of a room
  }
}
