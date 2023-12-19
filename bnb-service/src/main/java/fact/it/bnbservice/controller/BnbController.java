package fact.it.bnbservice.controller;


import fact.it.bnbservice.dto.BnbResponse;
import fact.it.bnbservice.service.BnbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bnb/")
@RequiredArgsConstructor
public class BnbController {

    private final BnbService bnbService;

    @GetMapping("/getBnb")
    @ResponseStatus(HttpStatus.OK)
    public List<BnbResponse> getBnbByName
            (@RequestParam String str) {
        return bnbService.getBnbsByName(str);
    }

    @GetMapping("/getAllBnb")
    @ResponseStatus(HttpStatus.OK)
    public List<BnbResponse> getAllBnb(){
        return bnbService.getAllBnbs();
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test";
    }
}
