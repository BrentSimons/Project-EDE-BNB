package fact.it.bnbservice.controller;

import fact.it.bnbservice.dto.BnbResponse;
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

    @GetMapping("/secureTest")
    @ResponseStatus(HttpStatus.OK)
    public String secureTest() {
        return "Secured test";
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test";
    }
}
