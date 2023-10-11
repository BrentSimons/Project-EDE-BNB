package fact.it.personservice.model;

// A class to represent a contact

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Contact {
    // Instance variables
    private String phoneNumber;
    private String emailAddress;
    private String address;
}