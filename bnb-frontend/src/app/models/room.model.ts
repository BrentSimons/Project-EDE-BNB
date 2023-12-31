export interface Room {
  id: string;
  roomCode: string;
  name: string;
  size: number;
}

export interface RoomCreate {
  roomRequest: Room;
  bnbId: number;
}
