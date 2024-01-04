package fact.it.roomservice.controller;

import fact.it.roomservice.dto.*;
import fact.it.roomservice.model.Room;
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


    // CRUD
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponse getRoom(@PathVariable String id) {
        return roomService.getRoom(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room createRoom(@RequestBody RoomWithBnbRequest roomWithBnbRequest) {
        return roomService.createRoom(roomWithBnbRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Room updateReservation(@PathVariable String id, @RequestBody RoomRequest updatedRoom) {
        return roomService.updateRoom(id, updatedRoom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable String id, @RequestParam Long bnbId) {
        roomService.deleteRoom(id, bnbId);
    }
}
