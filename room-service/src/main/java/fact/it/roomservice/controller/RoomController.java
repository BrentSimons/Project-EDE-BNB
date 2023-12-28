package fact.it.roomservice.controller;

import fact.it.roomservice.dto.AvailableRoomRequest;
import fact.it.roomservice.dto.AvailableRoomResponse;
import fact.it.roomservice.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableRoomResponse> getAvailableRooms(@RequestBody AvailableRoomRequest roomRequest) {
        return roomService.checkAvailability(roomRequest);
    }
}
