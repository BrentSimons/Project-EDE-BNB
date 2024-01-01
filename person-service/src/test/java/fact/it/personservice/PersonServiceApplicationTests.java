package fact.it.personservice;

import fact.it.personservice.dto.PersonRequest;
import fact.it.personservice.dto.PersonResponse;
import fact.it.personservice.model.Contact;
import fact.it.personservice.model.Person;
import fact.it.personservice.repository.PersonRepository;
import fact.it.personservice.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceApplicationTests {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Test
    public void testCreatePerson() {
        // Arrange
        PersonRequest personRequest = PersonRequest.builder()
                .firstName("Person")
                .lastName("Tester")
                .dateOfBirth(LocalDate.of(1990, 10, 3))
                .accountNumber("1003")
                .contact(
                        Contact.builder()
                                .phoneNumber("0420 69 69 69")
                                .address("Test Street 24")
                                .emailAddress("test@testmail.com")
                                .build())
                .build();

        PersonRequest defaultPerson = new PersonRequest();

        // Act
        personService.createPerson(personRequest);

        // Assert
        assertNotNull(defaultPerson);
        assertNull(defaultPerson.getFirstName());

        verify(personRepository, times(1)).save(any(Person.class));

    }

    @Test
    public void testGetAllPerson() {
        // Arrange
        Person person1 = Person.builder()
                .id("70")
                .firstName("Person1")
                .lastName("Tester")
                .dateOfBirth(LocalDate.of(1990, 10, 3))
                .accountNumber("1003")
                .contact(
                        Contact.builder()
                                .phoneNumber("0420 69 69 69")
                                .build())
                .build();
        Person person2 = Person.builder()
                .id("71")
                .firstName("Person2")
                .lastName("Tester")
                .dateOfBirth(LocalDate.of(1990, 10, 3))
                .accountNumber("1003")
                .contact(
                        Contact.builder()
                                .phoneNumber("0420 69 69 69")
                                .build())
                .build();

        when(personRepository.findAll()).thenReturn(Arrays.asList(person1, person2));

        // Act
        List<PersonResponse> personResponses = personService.getAllPerson();

        // Assert
        assertEquals(2, personResponses.size());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    public void testGetPerson() {
        // Arrange
        Contact contact = new Contact();
        contact.setPhoneNumber("0420 69 69 69");

        Person person = new Person();
        person.setId("69");
        person.setFirstName("Person");
        person.setLastName("Tester");
        person.setDateOfBirth(LocalDate.of(1990, 10, 3));
        person.setAccountNumber("1003");
        person.setContact(contact);

        personRepository.save(person);
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        // Act
        PersonResponse personResponse = personService.getPerson(person.getId());
        // No person found
        PersonResponse personResponse1 = personService.getPerson("2023");

        // Assert
        assertEquals("Person Tester", person.getFullName());

        assertEquals("Person", personResponse.getFirstName());
        assertEquals("Tester", personResponse.getLastName());
        assertEquals(LocalDate.of(1990, 10, 3), personResponse.getDateOfBirth());
        assertEquals("1003", personResponse.getAccountNumber());
        assertEquals("0420 69 69 69", personResponse.getContact().getPhoneNumber());
        assertNull(personResponse.getContact().getEmailAddress());
        assertNull(personResponse.getContact().getAddress());

        assertNull(personResponse1);
    }

    @Test
    public void testUpdatePerson() {
        // Arrange
        Person person = Person.builder()
                .id("69")
                .firstName("Person")
                .lastName("Tester")
                .dateOfBirth(LocalDate.of(1990, 10, 3))
                .accountNumber("1003")
                .contact(
                        Contact.builder()
                                .phoneNumber("0420 69 69 69")
                                .build())
                .build();
        personRepository.save(person);
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        PersonRequest personRequest = PersonRequest.builder()
                .firstName("Updated")
                .build();

        // Act
        personService.updatePerson(person.getId(), personRequest);
        Optional<Person> response = personRepository.findById(person.getId());

        // Assert
        assertEquals("Updated", response.get().getFirstName());
    }

    @Test
    public void testDeletePerson() {
        // Arrange
        Person person = Person.builder()
                .id("69")
                .firstName("Person")
                .lastName("Tester")
                .dateOfBirth(LocalDate.of(1990, 10, 3))
                .accountNumber("1003")
                .contact(
                        Contact.builder()
                                .phoneNumber("0420 69 69 69")
                                .build())
                .build();
        personRepository.save(person);

        // Act
        personService.deletePerson(person.getId());

        // Assert
        verify(personRepository, times(1)).deleteById(person.getId());

    }
}
