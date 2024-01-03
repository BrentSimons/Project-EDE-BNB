package fact.it.roomservice;

import fact.it.roomservice.dto.*;
import fact.it.roomservice.model.Room;
import fact.it.roomservice.repository.RoomRepository;
import fact.it.roomservice.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
class RoomServiceApplicationTests {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(roomService, "reservationServiceBaseUrl", "http://localhost:8090");
        ReflectionTestUtils.setField(roomService, "bnbServiceBaseUrl", "http://localhost:8080");
    }

    // The only purpose for roomService.checkRoomsAvailability() is removing all rooms that have a size that is less than the size assigned inside the AvailableRoomRequest
    // After removing rooms that are too small, the method makes a request to the reservation-service
    @Test
    public void testCheckRoomsAvailabilitySize3() {
        // Arrange
        Room room1 = Room.builder()
                .roomCode("Room1")
                .name("Room 1")
                .size(2)
                .build();
        Room room2 = Room.builder()
                .roomCode("Room2")
                .name("Room 2")
                .size(5)
                .build();
        // Both roomRequests created for testing have Room1 and Room2 in their roomCodes attribute
        AvailableRoomResponse responseRoom1 = AvailableRoomResponse.builder()
                .roomCode("Room1")
                .isAvailable(true)
                .build();
        AvailableRoomResponse responseRoom2 = AvailableRoomResponse.builder()
                .roomCode("Room2")
                .isAvailable(true)
                .build();


        AvailableRoomRequest roomRequestSize2 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .size(2)
                .startDate(LocalDate.of(2024, 3, 8))
                .endDate(LocalDate.of(2024, 3, 30))
                .build();
        AvailableRoomRequest roomRequestSize3 = AvailableRoomRequest.builder()
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .size(3)
                .startDate(LocalDate.of(2024, 3, 8))
                .endDate(LocalDate.of(2024, 3, 30))
                .build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Mockito.anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(AvailableRoomRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);


        // Act
        // roomRequestSize2 has a size of 2 so both room1 and room2 are returned as available (both rooms have a size greater than or equal to 2)
        when(roomRepository.findByRoomCodeInAndSizeGreaterThan(roomRequestSize2.getRoomCodes(), roomRequestSize2.getSize()-1)).thenReturn(Arrays.asList(room1, room2));
        when(responseSpec.bodyToMono(AvailableRoomResponse[].class)).thenReturn(Mono.just(new AvailableRoomResponse[]{responseRoom1, responseRoom2}));
        List<AvailableRoomResponse> responseSize2 = roomService.checkRoomsAvailability(roomRequestSize2);

        // roomRequestSize3 has a size of 3 so only room2 is returned (room1 has a size less than 3, namely 2)
        when(roomRepository.findByRoomCodeInAndSizeGreaterThan(roomRequestSize3.getRoomCodes(), roomRequestSize3.getSize()-1)).thenReturn(Arrays.asList(room2));
        when(responseSpec.bodyToMono(AvailableRoomResponse[].class)).thenReturn(Mono.just(new AvailableRoomResponse[]{responseRoom2}));
        List<AvailableRoomResponse> responseSize3 = roomService.checkRoomsAvailability(roomRequestSize3);

        // Arrange
        assertEquals(2, responseSize2.size());
        // Both rooms are returned when a size of 2 is requested
        assertTrue(responseSize2.contains(responseRoom1));
        assertTrue(responseSize2.contains(responseRoom2));

        assertEquals(1, responseSize3.size());
        // Room1 is not returned when a size of 3 is requested, Room2 is returned
        assertFalse(responseSize3.contains(responseRoom1));
        assertTrue(responseSize3.contains(responseRoom2));
    }

    @Test
    public void testGetAvailablePeriods() {
        // Arrange
        ReservationPeriod reservationPeriod = ReservationPeriod.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(2))
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ReservationPeriod[].class)).thenReturn(Mono.just(new ReservationPeriod[]{reservationPeriod}));


        // Act
        List<ReservationPeriod> result = roomService.getAvailablePeriods("Room1", 2);

        // Assert
        // There are no reservations so the result has startDate of today and endDate of 2 months from now
        assertEquals(1, result.size());
    }

    @Test
    public void testCreateRoom() {
        // Arrange
        RoomRequest roomRequest = RoomRequest.builder()
                .roomCode("Room1")
                .name("Room 1")
                .size(2)
                .build();
        RoomWithBnbRequest roomWithBnbRequest = RoomWithBnbRequest.builder()
                .roomRequest(roomRequest)
                .bnbId(2L)
                .build();

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        // Act
        Room result1 = roomService.createRoom(roomWithBnbRequest);

        // Assert
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    public void testGetAllRooms() {
        // Arrange
        Room room1 = Room.builder()
                .roomCode("Room1")
                .name("Room 1")
                .size(2)
                .build();
        Room room2 = Room.builder()
                .roomCode("Room2")
                .name("Room 2")
                .size(5)
                .build();

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        // Act
        List<RoomResponse> roomResponses = roomService.getAllRooms();

        // Assert
        assertEquals(2, roomResponses.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    public void testGetRoom() {
        // Arrange
        Room room = Room.builder()
                .roomCode("Room1")
                .name("Room 1")
                .size(3)
                .build();

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        // Act
        RoomResponse roomResponse1 = roomService.getRoom(room.getId());
        RoomResponse roomResponse2 = roomService.getRoom("23204");

        // Assert
        assertEquals("Room1", roomResponse1.getRoomCode());
        assertEquals("Room 1", roomResponse1.getName());
        assertEquals(3, roomResponse1.getSize());

        assertNull(roomResponse2);
    }

    @Test
    public void testUpdateRoom() {
        // Arrange
        Room room = Room.builder()
                .id("69")
                .roomCode("Room1")
                .name("Room 1")
                .size(2)
                .build();

        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        RoomRequest roomRequest = RoomRequest.builder()
                .name("New name for Room 1")
                .build();

        // Act
        roomService.updateRoom(room.getId(), roomRequest);
        Optional<Room> response = roomRepository.findById(room.getId());
        Room nullResponse = roomService.updateRoom("ThisIsAnInvalidId", roomRequest);

        // Assert
        assertEquals("New name for Room 1", response.get().getName());
        assertNull(nullResponse);
    }

    @Test
    public void testDeleteRoom() {
        // Arrange
        Room room = Room.builder()
                .id("69")
                .roomCode("Room1")
                .name("Room 1")
                .size(2)
                .build();
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        // Act
        roomService.deleteRoom(room.getId(), 2);

        // Assert
        verify(roomRepository, times(1)).deleteById(room.getId());
    }
}