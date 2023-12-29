package fact.it.roomservice.service;

import fact.it.roomservice.dto.AvailableRoomRequest;
import fact.it.roomservice.dto.AvailableRoomResponse;
import fact.it.roomservice.dto.RoomResponse;
import fact.it.roomservice.model.Room;
import fact.it.roomservice.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final WebClient webClient;

    @Value("${reservationservice.baseurl}")
    private String reservationServiceBaseUrl;

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

    public List<AvailableRoomResponse> checkAvailability(AvailableRoomRequest roomRequest) {

        List<String> roomCodes = roomRepository.findByRoomCodeInAndSizeGreaterThan(roomRequest.getRoomCodes(), roomRequest.getSize() - 1)
                .stream()
                .map(Room::getRoomCode)
                .toList();

        roomRequest.setRoomCodes(roomCodes);
        AvailableRoomResponse[] availableRoomResponseList = webClient.post()
                .uri("http://" + reservationServiceBaseUrl + "/api/reservation/available")
                .bodyValue(roomRequest)
                .retrieve()
                .bodyToMono(AvailableRoomResponse[].class)
                .block();


        return Arrays.asList(availableRoomResponseList);
    }
}
