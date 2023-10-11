package fact.it.personservice.dto;

import fact.it.personservice.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
    private String id;
    // Instance variables
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int accountNumber;
    // A Contact object as a composition
    private Contact contact;
}
