package fact.it.personservice.service;

import fact.it.personservice.repository.PersonRepository;
import fact.it.personservice.dto.PersonRequest;
import fact.it.personservice.dto.PersonResponse;
import fact.it.personservice.model.Contact;
import fact.it.personservice.model.Person;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    @PostConstruct
    public void loadData() {
        if (personRepository.count() <= 0) {

            Contact adamContact = new Contact("0589 69 14 32", "adamjohns23@heavenmail.com", "AngelStreet 26 Godtown");
            Contact eveContact = new Contact("1458 13 69 17", "evesmiths@heavenmail.com", "CloudStreet 1 Heavens-End");

            Person adam = Person.builder()
                    .firstName("Adam")
                    .lastName("Johns")
                    .id("person_1")
                    .accountNumber("1000")
                    .contact(adamContact)
                    .dateOfBirth(LocalDate.now().minusYears(20).plusDays(ThreadLocalRandom.current().nextInt(0, 20 * 365))).build();


            Person eve = Person.builder()
                    .firstName("Eve")
                    .lastName("Smith")
                    .id("person_2")
                    .accountNumber("1001")
                    .contact(eveContact)
                    .dateOfBirth(LocalDate.now().minusYears(20).plusDays(ThreadLocalRandom.current().nextInt(0, 20 * 365))).build();

            personRepository.save(adam);
            personRepository.save(eve);
        }
    }

    public void createPerson(PersonRequest personRequest) {
        Person person = Person.builder()
                .firstName(personRequest.getFirstName())
                .lastName(personRequest.getLastName())
                .id(personRequest.getId())
                .accountNumber(personRequest.getAccountNumber())
                .contact(personRequest.getContact())
                .dateOfBirth(personRequest.getDateOfBirth()).build();

        personRepository.save(person);
    }

    public List<PersonResponse> getAllPerson() {
        List<Person> people = personRepository.findAll();

        return people.stream().map(this::mapToPersonResponse).toList();
    }

    private PersonResponse mapToPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .dateOfBirth(person.getDateOfBirth())
                .accountNumber(person.getAccountNumber())
                .contact(person.getContact()).build();
    }

    private PersonRequest mapToPersonRequest(Person person) {
        return PersonRequest.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .dateOfBirth(person.getDateOfBirth())
                .accountNumber(person.getAccountNumber())
                .contact(person.getContact()).build();
    }

    public Person updatePerson(String id, PersonRequest updatedPerson) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            person.setFirstName(updatedPerson.getFirstName() != null ? updatedPerson.getFirstName() : person.getFirstName());
            person.setLastName(updatedPerson.getLastName() != null ? updatedPerson.getLastName() : person.getLastName());
            person.setDateOfBirth(updatedPerson.getDateOfBirth() != null ? updatedPerson.getDateOfBirth() : person.getDateOfBirth());
            person.setAccountNumber(updatedPerson.getAccountNumber() != null ? updatedPerson.getAccountNumber() : person.getAccountNumber());
            person.setContact(updatedPerson.getContact() != null ? updatedPerson.getContact() : person.getContact());
            personRepository.save(person);
        }
        return null; // Handle not found case
    }

    public void deletePerson(String id) {
        personRepository.deleteById(id);
    }

    public PersonRequest getPerson(String id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            return mapToPersonRequest(person);
        }
        return null;
    }
}
