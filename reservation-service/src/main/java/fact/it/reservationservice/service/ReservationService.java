package fact.it.reservationservice.service;

import java.util.List;
import java.util.stream.Collectors;

import fact.it.reservationservice.dto.ReservationRequest;
import org.springframework.stereotype.Service;

import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.model.Reservation;
import fact.it.reservationservice.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    
    @PostConstruct
    public void loadData() {
        if (reservationRepository.count() <= 0) {
            Reservation reservation = new Reservation();
            reservation.setPersonId((long) 1);
            reservation.setRoomId((long) 1);

            reservationRepository.save(reservation);
        }
    }

    public void createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setPersonId(reservationRequest.getPersonId());
        reservation.setRoomId(reservationRequest.getRoomId());

        reservationRepository.save(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(reservation -> new ReservationResponse(
                    reservation.getPersonId(),
                    reservation.getRoomId()
                ))
                .collect(Collectors.toList());
    }
}
