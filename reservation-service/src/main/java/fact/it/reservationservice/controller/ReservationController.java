package fact.it.reservationservice.controller;

import java.util.Date;
import java.util.List;

import fact.it.reservationservice.dto.*;
import fact.it.reservationservice.model.Reservation;
import org.springframework.web.bind.annotation.*;

import fact.it.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "Reservation test OK";
    }


    @PostMapping("/availableRooms")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableRoomResponse> getRoomsAvailability(@RequestBody AvailableRoomRequest roomRequest) {
        return reservationService.checkRoomsAvailability(roomRequest);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationPeriod> getAvailablePeriods(
            @RequestParam String roomCode,
            @RequestParam(required = false, defaultValue = "1") int months) {
        return reservationService.getAvailablePeriods(roomCode, months);
    }

    // CRUD
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.createReservation(reservationRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reservation updateReservation(@PathVariable Long id, @RequestBody ReservationRequest updatedReservation) {
        return reservationService.updateReservation(id, updatedReservation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }
}
