package fact.it.personservice.controller;

import fact.it.personservice.dto.PersonRequest;
import fact.it.personservice.dto.PersonResponse;
import fact.it.personservice.model.Person;
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

    
    // CRUD
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonResponse> getAllProducts() {
        // return personService
        return personService.getAllPerson();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonResponse getPerson(@PathVariable String id) {
        return personService.getPerson(id);
    }
    
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createPerson(@RequestBody PersonRequest personRequest) {
        personService.createPerson(personRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Person updatePerson(@PathVariable String id, @RequestBody PersonRequest updatedPerson) {
        return personService.updatePerson(id, updatedPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
    }
}
