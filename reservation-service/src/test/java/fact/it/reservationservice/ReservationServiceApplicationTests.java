package fact.it.reservationservice;

import fact.it.reservationservice.dto.*;
import fact.it.reservationservice.model.Reservation;
import fact.it.reservationservice.repository.ReservationRepository;
import fact.it.reservationservice.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReservationServiceApplicationTests {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    public void testCheckRoomsAvailability() {
        // You can open ./roomAvailability_RESULTS_visualized.xlsx to get a visualisation of the situation that is simulated in this test

        // Arrange
        // Create reservations for rooms with roomCodes Room1 and Room2
        Reservation reservation1 = Reservation.builder()
                .personId("3")
                .roomCode("Room1")
                .startDate(LocalDate.of(2024, 5, 20))
                .endDate(LocalDate.of(2024, 5, 24))
                .build();
        Reservation reservation2 = Reservation.builder()
                .personId("3")
                .roomCode("Room1")
                .startDate(LocalDate.of(2024, 5, 30))
                .endDate(LocalDate.of(2024, 6, 2))
                .build();
        Reservation reservation3 = Reservation.builder()
                .personId("3")
                .roomCode("Room2")
                .startDate(LocalDate.of(2024, 5, 22))
                .endDate(LocalDate.of(2024, 5, 26))
                .build();

        // Create AvailableRoomRequest for Room1 and Room2 to see if they are available between May 25th and 28th
        AvailableRoomRequest availableRoomRequest1 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .startDate(LocalDate.of(2024, 5, 25))
                .endDate(LocalDate.of(2024, 5, 28))
                .build();
        // Create AvailableRoomRequest for Room1 and Room2 to see if they are available between May 27th and 29th
        AvailableRoomRequest availableRoomRequest2 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .startDate(LocalDate.of(2024, 5, 27))
                .endDate(LocalDate.of(2024, 5, 29))
                .build();
        // Create AvailableRoomRequest for Room1 and Room2 to see if they are available between May 23rd and 25th
        AvailableRoomRequest availableRoomRequest3 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .startDate(LocalDate.of(2024, 5, 23))
                .endDate(LocalDate.of(2024, 5, 25))
                .build();
        // Create AvailableRoomRequest for Room1 to see if it is available between May 23rd and 25th
        AvailableRoomRequest availableRoomRequest4 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1"))
                .startDate(LocalDate.of(2024, 5, 25))
                .endDate(LocalDate.of(2024, 5, 28))
                .build();

        when(reservationRepository.findByRoomCode("Room1")).thenReturn(Arrays.asList(reservation1, reservation2));
        when(reservationRepository.findByRoomCode("Room2")).thenReturn(Arrays.asList(reservation3));


        // Act
        Map<String, Boolean> result1 = reservationService.checkRoomsAvailability(availableRoomRequest1)
                .stream()
                .collect(Collectors.toMap(AvailableRoomResponse::getRoomCode, AvailableRoomResponse::isAvailable));
        Map<String, Boolean> result2 = reservationService.checkRoomsAvailability(availableRoomRequest2)
                .stream()
                .collect(Collectors.toMap(AvailableRoomResponse::getRoomCode, AvailableRoomResponse::isAvailable));
        Map<String, Boolean> result3 = reservationService.checkRoomsAvailability(availableRoomRequest3)
                .stream()
                .collect(Collectors.toMap(AvailableRoomResponse::getRoomCode, AvailableRoomResponse::isAvailable));
        Map<String, Boolean> result4 = reservationService.checkRoomsAvailability(availableRoomRequest4)
                .stream()
                .collect(Collectors.toMap(AvailableRoomResponse::getRoomCode, AvailableRoomResponse::isAvailable));


        // Assert
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
        assertEquals(2, result3.size());
        assertEquals(1, result4.size());
        assertEquals(1, result4.size());

        // result1 shows that Room1 is available and Room2 unavailable
        assertEquals(true, result1.get("Room1"));
        assertEquals(false, result1.get("Room2"));

        // result2 shows that both Room1 and Room2 are available
        assertEquals(true, result2.get("Room1"));
        assertEquals(true, result2.get("Room2"));

        // result3 shows that both Room1 and Room2 are unavailable
        assertEquals(false, result3.get("Room1"));
        assertEquals(false, result3.get("Room2"));

        // result4 shows that both Room1 is available and Room2 is not returned (since it wasn't in the initial roomCodes list)
        assertEquals(true, result4.get("Room1"));
        assertEquals(false, result4.containsKey("Room2"));
    }

    @Test
    public void testGetAvailablePeriods() {
        // Arrange
        // Create some reservations for Room1_Bnb2, we will then request the available periods for this room
        // Reservation from
        Reservation reservation1 = Reservation.builder()
                .personId("3")
                .roomCode("ReservationRoom")
                .startDate(LocalDate.of(2024, 2, 4))
                .endDate(LocalDate.of(2024, 2, 6))
                .build();
        Reservation reservation2 = Reservation.builder()
                .personId("3")
                .roomCode("ReservationRoom")
                .startDate(LocalDate.of(2024, 4, 24))
                .endDate(LocalDate.of(2024, 4, 29))
                .build();

        // Only reservation1 takes place within 2 months
        when(reservationRepository.findByRoomCodeAndStartDateInNextMonthOrderByStartDateAsc("ReservationRoom", LocalDate.now().plusMonths(2))).thenReturn(Arrays.asList(reservation1));
        // Both reservation1 and reservation2 take place within the next 4 months
        when(reservationRepository.findByRoomCodeAndStartDateInNextMonthOrderByStartDateAsc("ReservationRoom", LocalDate.now().plusMonths(4))).thenReturn(Arrays.asList(reservation1, reservation2));


        // Act
        // ReservationRoom has 2 reservations in total
        List<ReservationPeriod> availablePeriodsReservationRoom2Months = reservationService.getAvailablePeriods("ReservationRoom", 2);
        // Testing the months parameter
        List<ReservationPeriod> availablePeriodsReservationRoom4Months = reservationService.getAvailablePeriods("ReservationRoom", 4);
        // NoReservatonRoom has no reservations so the entire next month should be returned as available
        List<ReservationPeriod> availablePeriodsNoReservationRoom1Month = reservationService.getAvailablePeriods("NoReservationRoom", 1);


        // Assert
        // !!! These tests might fail since the getAvailablePeriods() method is depending on the current date so our dummy data from above might become outdated !!!
        ReservationPeriod firstPeriod, secondPeriod, thirdPeriod;

        // 1 reservation during this period -> 1 time gap -> 2 divided availablePeriods
        assertEquals(2, availablePeriodsReservationRoom2Months.size());
        firstPeriod = availablePeriodsReservationRoom2Months.get(0);
        secondPeriod = availablePeriodsReservationRoom2Months.get(1);

        // The first availablePeriod is the current date (there is no reservation for today)
        assertEquals(LocalDate.now(), firstPeriod.getStartDate());
        // The first availablePeriod ends on the startDate of the first reservation
        assertEquals(reservation1.getStartDate(), firstPeriod.getEndDate());
        // The second availablePeriod starts on the endDate of the first reservation
        assertEquals(reservation1.getEndDate(), secondPeriod.getStartDate());
        // The second and last availablePeriod ends 2 months from now
        assertEquals(LocalDate.now().plusMonths(2), secondPeriod.getEndDate());


        // 2 reservations during this period -> 2 time gaps -> 3 divided availablePeriods
        assertEquals(3, availablePeriodsReservationRoom4Months.size());
        firstPeriod = availablePeriodsReservationRoom4Months.get(0);
        secondPeriod = availablePeriodsReservationRoom4Months.get(1);
        thirdPeriod = availablePeriodsReservationRoom4Months.get(2);

        // The second availablePeriod ends on the startDate of the second reservation
        assertEquals(reservation2.getStartDate(), secondPeriod.getEndDate());
        // The third availablePeriod starts on the endDate of the second reservation
        assertEquals(reservation2.getEndDate(), thirdPeriod.getStartDate());
        // The third and last availablePeriod ends 4 months from now
        assertEquals(LocalDate.now().plusMonths(4), thirdPeriod.getEndDate());


        // 0 reservations during this period -> no time gaps -> 1 whole availablePeriod (the entire next month)
        assertEquals(1, availablePeriodsNoReservationRoom1Month.size());
        firstPeriod = availablePeriodsNoReservationRoom1Month.get(0);

        // The first and only availablePeriod ends 1 month from now
        assertEquals(LocalDate.now().plusMonths(1), firstPeriod.getEndDate());
    }

    @Test
    public void testCreateReservation() {
        // Arrange
        ReservationRequest reservationRequest = ReservationRequest.builder()
                .personId("2")
                .roomCode("Room1_Bnb2")
                .startDate(LocalDate.of(2024, 5, 2))
                .endDate(LocalDate.of(2024, 5, 4))
                .build();

        // Testing NoArgsConstructor
        ReservationRequest defaultReservation = new ReservationRequest();

        // Act
        reservationService.createReservation(reservationRequest);

        // Assert
        assertNotNull(defaultReservation);
        assertNull(defaultReservation.getRoomCode());

        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void testGetAllReservations() {
        // Arrange
        Reservation reservation1 = Reservation.builder()
                .personId("2")
                .roomCode("Room1_Bnb2")
                .startDate(LocalDate.of(2024, 5, 2))
                .endDate(LocalDate.of(2024, 5, 4))
                .build();
        Reservation reservation2 = Reservation.builder()
                .personId("5")
                .roomCode("Room3_Bnb2")
                .startDate(LocalDate.of(2024, 6, 21))
                .endDate(LocalDate.of(2024, 6, 24))
                .build();

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // Act
        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();

        // Assert
        assertEquals(2, reservationResponses.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testGetReservation() {
        // Arrange
        Reservation reservation = Reservation.builder()
                .personId("2")
                .roomCode("Room1_Bnb2")
                .startDate(LocalDate.of(2024, 5, 2))
                .endDate(LocalDate.of(2024, 5, 4))
                .build();
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        // Act
        ReservationResponse reservationResponse = reservationService.getReservation(reservation.getId());
        ReservationResponse reservationResponse1 = reservationService.getReservation(3433L);

        // Assert
        assertEquals("2", reservationResponse.getPersonId());
        assertEquals("Room1_Bnb2", reservationResponse.getRoomCode());
        assertEquals(LocalDate.of(2024, 5, 2), reservationResponse.getStartDate());
        assertEquals(LocalDate.of(2024, 5, 4), reservationResponse.getEndDate());

        assertNull(reservationResponse1);
    }

    @Test
    public void testUpdateReservation() {
        // Arrange
        Reservation reservation = Reservation.builder()
                .personId("2")
                .roomCode("Room1_Bnb2")
                .startDate(LocalDate.of(2024, 5, 2))
                .endDate(LocalDate.of(2024, 5, 4))
                .build();

        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        ReservationRequest reservationRequest = ReservationRequest.builder()
                .endDate(LocalDate.of(2024, 5, 6))
                .build();

        // Act
        reservationService.updateReservation(reservation.getId(), reservationRequest);
        Optional<Reservation> response = reservationRepository.findById(reservation.getId());

        // Assert
        assertEquals(6, response.get().getEndDate().getDayOfMonth());
    }

    @Test
    public void testDeleteReservation() {
        // Arrange
        Reservation reservation = Reservation.builder()
                .personId("2")
                .roomCode("Room1_Bnb2")
                .startDate(LocalDate.of(2024, 5, 2))
                .endDate(LocalDate.of(2024, 5, 4))
                .build();

        // Act
        reservationService.deleteReservation(reservation.getId());

        // Assert
        verify(reservationRepository, times(1)).deleteById(reservation.getId());
    }
}