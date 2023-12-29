package fact.it.roomservice.controller;

import fact.it.roomservice.dto.AvailableRoomRequest;
import fact.it.roomservice.dto.AvailableRoomResponse;
import fact.it.roomservice.dto.ReservationPeriod;
import fact.it.roomservice.dto.RoomResponse;
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
        return "Room test OK";
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PostMapping("/availableRooms")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableRoomResponse> getAvailableRooms(@RequestBody AvailableRoomRequest roomRequest) {
        return roomService.checkRoomsAvailability(roomRequest);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationPeriod> getAvailablePeriods(@RequestParam String roomCode, @RequestParam(required = false, defaultValue = "1") int months) {
        return roomService.getAvailablePeriods(roomCode, months);
    }
}
