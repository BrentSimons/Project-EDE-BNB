package fact.it.reservationservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fact.it.reservationservice.dto.AvailableRoomRequest;
import fact.it.reservationservice.dto.AvailableRoomResponse;
import fact.it.reservationservice.dto.ReservationRequest;
import org.springframework.stereotype.Service;

import fact.it.reservationservice.dto.ReservationResponse;
import fact.it.reservationservice.model.Reservation;
import fact.it.reservationservice.repository.ReservationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

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

    public List<AvailableRoomResponse> checkAvailability(@RequestBody AvailableRoomRequest roomRequest) {
        List<AvailableRoomResponse> availableRoomIds = new ArrayList<AvailableRoomResponse>();

        for (String roomId : roomRequest.getRoomIds()) {
            if (reservationRepository.findOverlappingReservations(roomId, roomRequest.getStartDate(), roomRequest.getEndDate()).isEmpty()) {
                // If there are no overlapping reservations, the room is available
                availableRoomIds.add(new AvailableRoomResponse(roomId, true));
            }
        }
        return availableRoomIds;
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
