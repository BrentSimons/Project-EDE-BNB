package fact.it.bnbservice.controller;

import fact.it.bnbservice.dto.AvailableRoomRequest;
import fact.it.bnbservice.dto.BnbResponse;
import fact.it.bnbservice.dto.AvailableRoomResponse;
import fact.it.bnbservice.service.BnbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bnb")
@RequiredArgsConstructor
public class BnbController {

    private final BnbService bnbService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<BnbResponse> getBnbByName(@RequestParam(required = false) String name) {
        if (name != null) {
            // Example: http://localhost:8080/api/bnb/get?name=Hugo
            return bnbService.getBnbsByName(name);
        } else {
            return bnbService.getAllBnbs();
        }
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<AvailableRoomResponse> getAvailableRooms(@RequestBody AvailableRoomRequest roomRequest, @RequestParam Long bnbId) {
        return bnbService.getAvailableRooms(roomRequest, bnbId);
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
}
