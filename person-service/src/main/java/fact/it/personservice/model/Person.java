package fact.it.personservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// A class to represent a person
@Document(value="person")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Person {
    private String id;
    // Instance variables
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    // TODO accountnumber should be String i think?
    private String accountNumber;
    // A Contact object as a composition
    private Contact contact;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}