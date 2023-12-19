package fact.it.reservationservice.controller;

import java.util.List;

import fact.it.reservationservice.dto.ReservationRequest;
import org.springframework.web.bind.annotation.*;

import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationResponse> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createReservation(@RequestBody ReservationRequest reservationRequest) {
        reservationService.createReservation(reservationRequest);
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test";
    }
}
