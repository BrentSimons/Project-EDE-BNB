package fact.it.reservationservice.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Reservation reservation1 = new Reservation();
            reservation1.setPersonId((long) 1);
            reservation1.setRoomId((long) 1);
            try {
                reservation1.setStartDate(format.parse("2024-05-12"));
                reservation1.setEndDate(format.parse("2024-05-13"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation2 = new Reservation();
            reservation2.setPersonId((long) 2);
            reservation2.setRoomId((long) 2);
            try {
                reservation2.setStartDate(format.parse("2024-08-21"));
                reservation2.setEndDate(format.parse("2024-08-22"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation3 = new Reservation();
            reservation3.setPersonId((long) 1);
            reservation3.setRoomId((long) 5);
            try {
                reservation3.setStartDate(format.parse("2024-03-13"));
                reservation3.setEndDate(format.parse("2024-03-14"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.save(reservation3);
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
                        reservation.getRoomId(),
                        reservation.getStartDate(),
                        reservation.getEndDate()
                )).toList();
    }
}
