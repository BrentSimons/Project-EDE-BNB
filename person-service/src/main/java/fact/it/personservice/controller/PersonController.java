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
    // Create Example

    /*
    {
        "id": "3",
            "firstName": "Jesus",
            "lastName": "Christ",
            "dateOfBirth": "0001-01-01",
            "accountNumber": 1003,
            "contact": {
                "phoneNumber": "911",
                "emailAddress": "JesusC@LordlyMail.co.heaven",
                "address": "Heavenly Palace of the Lord 2 Apartment 5, Heavens Gate"
                }
    }
    */

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllProducts() {
        //return personService
        return personService.getAllPerson();
    }
}
