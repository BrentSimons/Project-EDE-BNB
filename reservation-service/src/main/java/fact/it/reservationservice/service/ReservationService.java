package fact.it.reservationservice.service;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
            reservation1.setPersonId("1");
            reservation1.setRoomCode("Room1_Bnb1");
            try {
                reservation1.setStartDate(format.parse("2024-05-12"));
                reservation1.setEndDate(format.parse("2024-05-13"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation2 = new Reservation();
            reservation2.setPersonId("2");
            reservation2.setRoomCode("Room1_Bnb1");
            try {
                reservation2.setStartDate(format.parse("2024-03-10"));
                reservation2.setEndDate(format.parse("2024-03-15"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation3 = new Reservation();
            reservation3.setPersonId("1");
            reservation3.setRoomCode("Room2_Bnb1");
            try {
                reservation3.setStartDate(format.parse("2024-03-13"));
                reservation3.setEndDate(format.parse("2024-03-17"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation4 = new Reservation();
            reservation4.setPersonId("1");
            reservation4.setRoomCode("Room1_Bnb1");
            try {
                reservation4.setStartDate(format.parse("2024-03-19"));
                reservation4.setEndDate(format.parse("2024-03-21"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Reservation reservation5 = new Reservation();
            reservation5.setPersonId("1");
            reservation5.setRoomCode("Room2_Bnb1");
            try {
                reservation5.setStartDate(format.parse("2024-03-20"));
                reservation5.setEndDate(format.parse("2024-03-22"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.save(reservation3);
            reservationRepository.save(reservation4);
            reservationRepository.save(reservation5);
        }
    }

    public List<AvailableRoomResponse> checkAvailability(@RequestBody AvailableRoomRequest roomRequest) {
        List<AvailableRoomResponse> availableRooms = new ArrayList<AvailableRoomResponse>();

        // System.out.println("Large enough rooms: " + roomRequest.getRoomCodes());

        for (String roomCode : roomRequest.getRoomCodes()) {
            // System.out.println("Room: " + roomCode);

            boolean overlapping = false;
            List<Reservation> reservations = reservationRepository.findByRoomCode(roomCode);
            for (Reservation reservation : reservations) {
                // System.out.println("Checking reservation for " + reservation.getStartDate() + " and " + reservation.getEndDate());

                if (roomRequest.getStartDate().before(roomRequest.getEndDate())) {
                    // request startDate before request endDate
                    if (roomRequest.getEndDate().before(reservation.getStartDate()) || roomRequest.getStartDate().after(reservation.getEndDate())) {
                        // request endDate before reservation startDate OR request startDate after reservation endDate
                        // System.out.println("Room has valid dates");
                        continue;
                    }
                }
                overlapping = true;
            }

            if (!overlapping) {
                System.out.println("Valid reservation for " + roomCode);
            }
            availableRooms.add(AvailableRoomResponse.builder()
                    .roomCode(roomCode)
                    .isAvailable(!overlapping)
                    .build());
        }

//        for (String roomId : roomRequest.getRoomCodes()) {
//            if (reservationRepository.findOverlappingReservations(roomId, roomRequest.getStartDate(), roomRequest.getEndDate()).isEmpty()) {
//                // If there are no overlapping reservations, the room is available
//                availableRoomIds.add(new AvailableRoomResponse(roomId, true));
//            }
//        }

        return availableRooms;
    }


    public void createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setPersonId(reservationRequest.getPersonId());
        reservation.setRoomCode(reservationRequest.getRoomCode());

        reservationRepository.save(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getPersonId(),
                        reservation.getRoomCode(),
                        reservation.getStartDate(),
                        reservation.getEndDate()
                )).toList();
    }
}
