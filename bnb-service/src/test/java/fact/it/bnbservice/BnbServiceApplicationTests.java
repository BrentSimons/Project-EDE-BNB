package fact.it.bnbservice; // Assuming the package name is similar

import fact.it.bnbservice.dto.AvailableRoomRequest;
import fact.it.bnbservice.dto.AvailableRoomResponse;
import fact.it.bnbservice.dto.BnbRequest; // Assuming similar DTOs
import fact.it.bnbservice.dto.BnbResponse; // Assuming similar DTOs
import fact.it.bnbservice.model.Bnb; // Assuming similar model
import fact.it.bnbservice.repository.BnbRepository; // Assuming similar repository
import fact.it.bnbservice.service.BnbService; // Assuming similar service
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BnbServiceApplicationTests {

    @InjectMocks
    private BnbService bnbService;

    @Mock
    private BnbRepository bnbRepository;

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
        ReflectionTestUtils.setField(bnbService, "roomServiceBaseUrl", "http://localhost:8090");
    }


    @Test
    public void testGetAvailableRooms() {
        // getAvailableRooms() method's main purpose is dynamically retrieving roomCodes of a certain bnb and passing this data on to the roomService

        // Arrange
        Bnb bnb1 = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1_Bnb1", "Room2_Bnb1", "Room3_Bnb1"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();
        List<AvailableRoomResponse> availableRoomResponsesBnb1 = List.of(
                AvailableRoomResponse.builder().roomCode("Room1_Bnb1").isAvailable(true).build(),
                AvailableRoomResponse.builder().roomCode("Room2_Bnb1").isAvailable(true).build(),
                AvailableRoomResponse.builder().roomCode("Room2_Bnb1").isAvailable(true).build()
        );

        Bnb bnb2 = Bnb.builder()
                .id(70L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1_Bnb2"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();
        List<AvailableRoomResponse> availableRoomResponsesBnb2 = List.of(
                AvailableRoomResponse.builder().roomCode("Room1_Bnb2").isAvailable(true).build()
        );

        AvailableRoomRequest roomRequest = AvailableRoomRequest.builder()
                .roomCodes(List.of())
                .startDate(LocalDate.of(2024, 4, 20))
                .endDate(LocalDate.of(2024, 4, 30))
                .size(3)
                .build();

        when(bnbRepository.findById(bnb1.getId())).thenReturn(Optional.of(bnb1));
        when(bnbRepository.findById(bnb2.getId())).thenReturn(Optional.of(bnb2));

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Mockito.anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(AvailableRoomRequest.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // Act
        when(responseSpec.bodyToMono(AvailableRoomResponse[].class)).thenReturn(Mono.just(availableRoomResponsesBnb1.toArray(new AvailableRoomResponse[0])));
        List<AvailableRoomResponse> responseBnb1 = bnbService.getAvailableRooms(roomRequest, 69L);

        when(responseSpec.bodyToMono(AvailableRoomResponse[].class)).thenReturn(Mono.just(availableRoomResponsesBnb2.toArray(new AvailableRoomResponse[0])));
        List<AvailableRoomResponse> responseBnb2 = bnbService.getAvailableRooms(roomRequest, 70L);

        // Assert
        // Bnb1 has 3 rooms and since we cannot check on size of a room or whether there are reservations for certain rooms our method will return all rooms as available
        assertEquals(3, responseBnb1.size());
        assertTrue(bnb1.getRoomCodes().containsAll(responseBnb1.stream().map(AvailableRoomResponse::getRoomCode).toList()));

        // Bnb1 has 1 room and since we cannot check on size of a room or whether there are reservations for certain rooms our method will return all rooms as available
        assertEquals(1, responseBnb2.size());
        assertTrue(bnb2.getRoomCodes().containsAll(responseBnb2.stream().map(AvailableRoomResponse::getRoomCode).toList()));
    }

    @Test
    public void testAddRoom() {
        // Arrange
        Bnb bnb = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        when(bnbRepository.findById(bnb.getId())).thenReturn(Optional.of(bnb));

        // Act
        boolean resultValidBnbId = bnbService.addRoom(bnb.getId(), "NewRoom");
        boolean resultInvalidBnbId = bnbService.addRoom(2932932L, "NewRoom");

        // Assert
        assertEquals(4, bnbRepository.findById(bnb.getId()).get().getRoomCodes().size());
        assertTrue(resultValidBnbId);
        assertFalse(resultInvalidBnbId);
    }

    @Test
    public void testRemoveRoomFromBnb() {
        // Arrange
        Bnb bnb = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3", "NewRoom"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        when(bnbRepository.findById(bnb.getId())).thenReturn(Optional.of(bnb));

        // Act
        boolean resultValidBnbId = bnbService.removeRoomFromBnb(bnb.getId(), "NewRoom");
        boolean resultValidBnbIdD = bnbService.removeRoomFromBnb(bnb.getId(), "TEST");
        boolean resultInvalidBnbId = bnbService.addRoom(2932932L, "NewRoom");

        // Assert
        assertEquals(3, bnbRepository.findById(bnb.getId()).get().getRoomCodes().size());
        assertTrue(resultValidBnbId);
        assertFalse(resultInvalidBnbId);
    }

    @Test
    public void testCreateBnb() {
        // Arrange
        BnbRequest bnbRequest = BnbRequest.builder()
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        // Testing NoArgsConstructor
        BnbRequest defaultBnb = new BnbRequest();

        // Act
        bnbService.createBnb(bnbRequest);

        // Assert
        assertNotNull(defaultBnb);
        assertNull(defaultBnb.getName());

        verify(bnbRepository, times(1)).save(any(Bnb.class));
    }

    @Test
    public void testGetAllBnbs() {
        // Arrange
        Bnb bnb1 = Bnb.builder()
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();
        Bnb bnb2 = Bnb.builder()
                .name("Bnb2")
                .roomCodes(Arrays.asList("Room1", "Room2"))
                .city("Geel")
                .postcode("2440")
                .address("Antwerpseweg 117")
                .build();

        // getBnbsByNameContains returns all bnb's if the value given for the name parameter is ""
        when(bnbRepository.getBnbsByNameContains("")).thenReturn(List.of(bnb1, bnb2));

        // Act
        List<BnbResponse> bnbResponses = bnbService.getBnbsByName("");

        // Assert
        assertEquals(2, bnbResponses.size());
        verify(bnbRepository, times(1)).getBnbsByNameContains("");
    }

    @Test
    public void testGetBnb() {
        // Arrange
        Bnb bnb = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        when(bnbRepository.findById(bnb.getId())).thenReturn(Optional.of(bnb));

        // Act
        BnbResponse bnbResponse = bnbService.getBnb(bnb.getId());
        // Test for invalid ID
        BnbResponse bnbResponse1 = bnbService.getBnb(2032003L);

        // Assert
        assertEquals("Bnb1", bnbResponse.getName());
        assertEquals(Arrays.asList("Room1", "Room2", "Room3"), bnbResponse.getRoomCodes());
        assertEquals("Geel", bnbResponse.getCity());
        assertEquals("2440", bnbResponse.getPostcode());
        assertEquals("Drijhoek", bnbResponse.getAddress());

        assertNull(bnbResponse1);
    }

    @Test
    public void testGetBnbByName() {
        // Arrange
        Bnb bnbH1 = Bnb.builder()
                .name("Bnb1 met H")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();
        Bnb bnbH2 = Bnb.builder()
                .name("Bnb2 met H")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();
        Bnb bnbL1 = Bnb.builder()
                .name("Bnb1 met L")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        when(bnbRepository.getBnbsByNameContains("H")).thenReturn(Arrays.asList(bnbH1, bnbH2));
        when(bnbRepository.getBnbsByNameContains("L")).thenReturn(Arrays.asList(bnbL1));

        // Act
        List<BnbResponse> responsesH = bnbService.getBnbsByName("H");
        List<BnbResponse> responsesL = bnbService.getBnbsByName("L");

        // Assert
        assertEquals(2, responsesH.size());
        assertEquals(1, responsesL.size());
    }

    @Test
    public void testUpdateBnb() {
        // Arrange
        Bnb bnb = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        when(bnbRepository.findById(bnb.getId())).thenReturn(Optional.of(bnb));

        BnbRequest bnbRequest = BnbRequest.builder()
                .name("New name for Bnb1")
                .build();

        // Act
        bnbService.updateBnb(bnb.getId(), bnbRequest);
        Optional<Bnb> response = bnbRepository.findById(bnb.getId());
        Bnb nullResponse = bnbService.updateBnb(2329320L, bnbRequest);

        // Assert
        assertEquals("New name for Bnb1", response.get().getName());
        assertNull(nullResponse);
    }

    @Test
    public void testDeleteBnb() {
        // Arrange
        Bnb bnb = Bnb.builder()
                .id(69L)
                .name("Bnb1")
                .roomCodes(Arrays.asList("Room1", "Room2", "Room3"))
                .city("Geel")
                .postcode("2440")
                .address("Drijhoek")
                .build();

        // Act
        bnbService.deleteBnb(bnb.getId());

        // Assert
        verify(bnbRepository, times(1)).deleteById(bnb.getId());
    }
}