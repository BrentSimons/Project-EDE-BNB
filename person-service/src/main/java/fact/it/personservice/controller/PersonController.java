package fact.it.personservice.controller;

import fact.it.personservice.dto.PersonRequest;
import fact.it.personservice.dto.PersonResponse;
import fact.it.personservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createPerson(@RequestBody PersonRequest personRequest) {
        personService.createPerson(personRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllProducts() {
        // return personService
        return personService.getAllPerson();
    }

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "test";
    }
}
