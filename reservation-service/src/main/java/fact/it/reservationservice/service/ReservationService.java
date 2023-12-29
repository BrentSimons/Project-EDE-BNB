package fact.it.reservationservice.service;

import java.io.Console;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fact.it.reservationservice.dto.*;
import org.springframework.stereotype.Service;

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Reservation reservation1 = new Reservation();
            reservation1.setPersonId("1");
            reservation1.setRoomCode("Room1_Bnb1");
            reservation1.setStartDate(Date.from(LocalDate.parse("2024-05-12", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation1.setEndDate(Date.from(LocalDate.parse("2024-05-13", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Reservation reservation2 = new Reservation();
            reservation2.setPersonId("2");
            reservation2.setRoomCode("Room1_Bnb1");
            reservation2.setStartDate(Date.from(LocalDate.parse("2024-03-10", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation2.setEndDate(Date.from(LocalDate.parse("2024-03-15", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Reservation reservation3 = new Reservation();
            reservation3.setPersonId("1");
            reservation3.setRoomCode("Room2_Bnb1");
            reservation3.setStartDate(Date.from(LocalDate.parse("2024-03-13", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation3.setEndDate(Date.from(LocalDate.parse("2024-03-17", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Reservation reservation4 = new Reservation();
            reservation4.setPersonId("1");
            reservation4.setRoomCode("Room1_Bnb1");
            reservation4.setStartDate(Date.from(LocalDate.parse("2024-03-19", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation4.setEndDate(Date.from(LocalDate.parse("2024-03-21", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Reservation reservation5 = new Reservation();
            reservation5.setPersonId("1");
            reservation5.setRoomCode("Room2_Bnb1");
            reservation5.setStartDate(Date.from(LocalDate.parse("2024-03-20", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation5.setEndDate(Date.from(LocalDate.parse("2024-03-22", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Reservation reservation6 = new Reservation();
            reservation6.setPersonId("1");
            reservation6.setRoomCode("Room2_Bnb1");
            reservation6.setStartDate(Date.from(LocalDate.parse("2024-01-20", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
            reservation6.setEndDate(Date.from(LocalDate.parse("2024-01-22", formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));

            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.save(reservation3);
            reservationRepository.save(reservation4);
            reservationRepository.save(reservation5);
            reservationRepository.save(reservation6);
        }
    }

    public List<AvailableRoomResponse> checkRoomsAvailability(@RequestBody AvailableRoomRequest roomRequest) {
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

            /*if (!overlapping) {
                System.out.println("Valid reservation for " + roomCode);
            }*/
            availableRooms.add(AvailableRoomResponse.builder()
                    .roomCode(roomCode)
                    .isAvailable(!overlapping)
                    .build());
        }

        return availableRooms;
    }

    public List<ReservationPeriod> getAvailablePeriods(String roomCode, int months) {
        LocalDate localDate = LocalDate.now();
        LocalDate nextMonth = localDate.plusMonths(months);
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date nextMonthDate = Date.from(nextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Reservation> reservations = reservationRepository.findByRoomCodeAndStartDateInNextMonthOrderByStartDateAsc(roomCode, nextMonthDate);
        List<ReservationPeriod> reservationPeriods = new ArrayList<>();

        if (!reservations.isEmpty()) {
            // Add the period from the beginning of the month to the first reservation
            Reservation firstReservation = reservations.get(0);
            reservationPeriods.add(new ReservationPeriod(currentDate, firstReservation.getStartDate()));

            // Add the periods between consecutive reservations
            for (int i = 0; i < reservations.size() - 1; i++) {
                Reservation currentReservation = reservations.get(i);
                Reservation nextReservation = reservations.get(i + 1);

                reservationPeriods.add(new ReservationPeriod(currentReservation.getEndDate(), nextReservation.getStartDate()));
            }

            // Add the period from the last reservation to the end of the month
            Reservation lastReservation = reservations.get(reservations.size() - 1);
            reservationPeriods.add(new ReservationPeriod(lastReservation.getEndDate(), nextMonthDate));
        } else {
            // If there are no reservations, add the entire month as an available period
            reservationPeriods.add(new ReservationPeriod(currentDate, nextMonthDate));
        }

        return reservationPeriods;
    }

    // CRUD
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

    public ReservationResponse getReservation(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            return ReservationResponse.builder()
                    .personId(reservation.getPersonId())
                    .roomCode(reservation.getRoomCode())
                    .startDate(reservation.getStartDate())
                    .endDate(reservation.getEndDate())
                    .build();
        }
        return null;
    }

    public Reservation createReservation(ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setPersonId(reservationRequest.getPersonId());
        reservation.setRoomCode(reservationRequest.getRoomCode());
        reservation.setStartDate(reservationRequest.getStartDate());
        reservation.setEndDate(reservationRequest.getEndDate());

        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation updateReservation(Long id, ReservationRequest updatedReservation) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();

            reservation.setPersonId(updatedReservation.getPersonId() != null ? updatedReservation.getPersonId() : reservation.getPersonId());
            reservation.setRoomCode(updatedReservation.getRoomCode() != null ? updatedReservation.getRoomCode() : reservation.getRoomCode());
            reservation.setStartDate(updatedReservation.getStartDate() != null ? updatedReservation.getStartDate() : reservation.getStartDate());
            reservation.setEndDate(updatedReservation.getEndDate() != null ? updatedReservation.getEndDate() : reservation.getEndDate());
            return reservationRepository.save(reservation);
        }
        return null; // Handle not found case
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
