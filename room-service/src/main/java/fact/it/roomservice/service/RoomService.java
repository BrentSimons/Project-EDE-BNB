package fact.it.roomservice.service;

import fact.it.roomservice.dto.*;
import fact.it.roomservice.model.Room;
import fact.it.roomservice.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final WebClient webClient;

    @Value("${reservationservice.baseurl}")
    private String reservationServiceBaseUrl;

    @Value("${bnbservice.baseurl}")
    private String bnbServiceBaseUrl;

    @PostConstruct
    public void loadData() {
        if (roomRepository.count() <= 0) {
            // Hugo's Bnb Ter Dolen
            Room room1 = new Room();
            room1.setName("Room1");
            room1.setRoomCode("Room1_Bnb1");
            room1.setSize(5);

            Room room2 = new Room();
            room2.setName("Room2");
            room2.setRoomCode("Room2_Bnb1");
            room2.setSize(3);

            Room room3 = new Room();
            room3.setName("Room3");
            room3.setRoomCode("Room3_Bnb1");
            room3.setSize(2);

            // Hugo's Bnb Geel
            Room room4 = new Room();
            room4.setName("Room4");
            room4.setRoomCode("Room1_Bnb2");
            room4.setSize(4);

            Room room5 = new Room();
            room5.setName("Room5");
            room5.setRoomCode("Room2_Bnb2");
            room5.setSize(2);

            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);
            roomRepository.save(room4);
            roomRepository.save(room5);
        }
    }

    public List<AvailableRoomResponse> checkRoomsAvailability(AvailableRoomRequest roomRequest) {
        // Update roomRequest so that it only contains rooms that are big enough
        List<String> roomCodes = roomRepository.findByRoomCodeInAndSizeGreaterThan(roomRequest.getRoomCodes(), roomRequest.getSize() - 1)
                .stream()
                .map(Room::getRoomCode)
                .toList();

        roomRequest.setRoomCodes(roomCodes);

        // Call Reservation service to check for every room if there already are reservations during the time period given in roomRequest
        AvailableRoomResponse[] availableRoomResponseList = webClient.post()
                .uri("http://" + reservationServiceBaseUrl + "/api/reservation/availableRooms")
                .bodyValue(roomRequest)
                .retrieve()
                .bodyToMono(AvailableRoomResponse[].class)
                .block();

        return Arrays.asList(availableRoomResponseList != null ? availableRoomResponseList : new AvailableRoomResponse[0]);
    }

    public List<ReservationPeriod> getAvailablePeriods(String roomCode, int months) {
        ReservationPeriod[] reservationPeriods = webClient.get()
                .uri("http://" + reservationServiceBaseUrl + "/api/reservation/available",
                        uriBuilder -> uriBuilder.queryParam("roomCode", roomCode).queryParam("months", months).build())
                .retrieve()
                .bodyToMono(ReservationPeriod[].class)
                .block();

        return Arrays.stream(reservationPeriods != null ? reservationPeriods : new ReservationPeriod[0]).toList();
    }

    // CRUD
    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getRoomCode(),
                        room.getName(),
                        room.getSize()
                )).toList();
    }

    public RoomResponse getRoom(String id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            return RoomResponse.builder()
                    .id(room.getId())
                    .roomCode(room.getRoomCode())
                    .name(room.getName())
                    .size(room.getSize())
                    .build();
        }
        return null;
    }

    public Room createRoom(RoomWithBnbRequest roomWithBnbRequest) {
        RoomRequest roomRequest = roomWithBnbRequest.getRoomRequest();

        Room room = Room.builder()
                .roomCode(roomRequest.getRoomCode())
                .name(roomRequest.getName())
                .size(roomRequest.getSize())
                .build();

        Long bnbId = roomWithBnbRequest.getBnbId();
        boolean result = Boolean.TRUE.equals(webClient.put()
                .uri("http://" + bnbServiceBaseUrl + "/api/bnb/addRoom",
                        uriBuilder -> uriBuilder.queryParam("bnbId", bnbId).queryParam("roomCode", roomRequest.getRoomCode()).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());

        if (Boolean.FALSE.equals(result)) {
            return null;
        }

        roomRepository.save(room);
        return room;
    }

    public Room updateRoom(String id, RoomRequest updatedRoom) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();

            room.setRoomCode(updatedRoom.getRoomCode() != null ? updatedRoom.getRoomCode() : room.getRoomCode());
            room.setName(updatedRoom.getName() != null ? updatedRoom.getName() : room.getName());
            room.setSize(updatedRoom.getSize() != 0 ? updatedRoom.getSize() : room.getSize());

            return roomRepository.save(room);
        }
        return null; // Handle not found case
    }

    public void deleteRoom(String id, int bnbId) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();

            // Delete the room from the Bnb
            boolean result = Boolean.TRUE.equals(webClient.put()
                    .uri("http://" + bnbServiceBaseUrl + "/api/bnb/removeRoom",
                            uriBuilder -> uriBuilder.queryParam("bnbId", bnbId).queryParam("roomCode", room.getRoomCode()).build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

            if (Boolean.FALSE.equals(result)) {
                // Handle the case where the room deletion from Bnb fails
                // You might want to log an error or throw an exception
            }

            // Delete the room from the local database
            roomRepository.deleteById(id);
        } else {
            // Handle the case where the room is not found
            // You might want to log an error or throw an exception
        }
    }
}

