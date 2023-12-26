package fact.it.roomservice.service;

import fact.it.roomservice.dto.AvailableRoomRequest;
import fact.it.roomservice.dto.AvailableRoomResponse;
import fact.it.roomservice.model.Room;
import fact.it.roomservice.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    @Value("${reservationservice.baseurl}")
    private String reservationServiceBaseUrl;

    @PostConstruct
    public void loadData() {
        if (roomRepository.count() <= 0) {

        }
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
