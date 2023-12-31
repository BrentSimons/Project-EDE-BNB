// reservation.model.ts
export interface Reservation {
  id: number;
  personId: string;
  roomCode: string;
  startDate: string; // Format: "yyyy-MM-dd"
  endDate: string; // Format: "yyyy-MM-dd"
}

export interface ReservationRequest {
  id?: number; // id optional for ReservationRequest
  personId: string;
  roomCode: string;
  startDate: string; // Format: "yyyy-MM-dd"
  endDate: string; // Format: "yyyy-MM-dd"
}
