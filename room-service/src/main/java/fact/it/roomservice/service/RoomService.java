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
            room1.setName("Room1_Bnb1");
            room1.setSize(5);

            Room room2 = new Room();
            room2.setName("Room2_Bnb1");
            room2.setSize(3);

            Room room3 = new Room();
            room3.setName("Room3_Bnb1");
            room3.setSize(2);

            // Hugo's Bnb Geel
            Room room4 = new Room();
            room4.setName("Room1_Bnb2");
            room4.setSize(4);

            Room room5 = new Room();
            room5.setName("Room2_Bnb2");
            room5.setSize(2);

            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);
            roomRepository.save(room4);
            roomRepository.save(room5);
        }
    }

    public String test() {
        String response = webClient.get()
                .uri("http://" + reservationServiceBaseUrl + "/api/reservation/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return response;
    }

    public List<RoomResponse> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getName(),
                        room.getSize()
                )).toList();
    }

    public List<AvailableRoomResponse> checkAvailability(AvailableRoomRequest roomRequest) {

        List<String> roomIds = roomRepository.findByIdInAndSizeGreaterThan(roomRequest.getRoomIds(), roomRequest.getSize())
                .stream()
                .map(Room::getId)
                .toList();

        roomRequest.setRoomIds(roomIds);
        // TODO call /api/reservation/available with the updated roomRequest
        return null;
    }
}
