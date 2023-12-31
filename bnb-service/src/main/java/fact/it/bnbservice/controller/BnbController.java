package fact.it.bnbservice.controller;

import fact.it.bnbservice.dto.AvailableRoomRequest;
import fact.it.bnbservice.dto.BnbResponse;
import fact.it.bnbservice.model.Bnb;
import fact.it.bnbservice.dto.AvailableRoomResponse;
import fact.it.bnbservice.dto.BnbRequest;
import fact.it.bnbservice.service.BnbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bnb")
@RequiredArgsConstructor
public class BnbController {

    private final BnbService bnbService;
    

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableRoomResponse> getAvailableRooms(@RequestBody AvailableRoomRequest roomRequest, @RequestParam Long id) {
        return bnbService.getAvailableRooms(roomRequest, id);
    }

    @PutMapping("/addRoom")
    @ResponseStatus(HttpStatus.OK)
    public boolean addRoom(@RequestParam Long bnbId, @RequestParam String roomCode) {
        return bnbService.addRoom(bnbId, roomCode);
    }

    @GetMapping("/secureTest")
    @ResponseStatus(HttpStatus.OK)
    public String secureTest() {
        return "Secured Bnb test OK";
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "Bnb test OK";
    }

    // CRUD
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<BnbResponse> getBnbByName(@RequestParam(required = false, defaultValue = "") String name) {
        // Example: http://localhost:8080/api/bnb/get?name=Hugo
        // If no name given, this method returns all bnbs found
        return bnbService.getBnbsByName(name);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BnbResponse getBnb(@PathVariable Long id) {
        return bnbService.getBnb(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bnb createBnb(@RequestBody BnbRequest bnbRequest) {
        return bnbService.createBnb(bnbRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Bnb updateBnb(@PathVariable Long id, @RequestBody BnbRequest updatedBnb) {
        return bnbService.updateBnb(id, updatedBnb);
    }

    @PutMapping("/removeRoom")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeRoom(@RequestParam Long bnbId, @RequestParam String roomCode) {
        boolean removed = bnbService.removeRoomFromBnb(bnbId, roomCode);
        return removed;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBnb(@PathVariable Long id) {
        bnbService.deleteBnb(id);
    }
}
