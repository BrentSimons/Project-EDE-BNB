package fact.it.bnbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BnbResponse {
<<<<<<< HEAD
    private String name;

    private List<String> roomCodes;

    private String city;

    private String postcode;

=======
    private Long id;

    private String name;

    private List<String> roomCodes;

    private String city;

    private String postcode;

>>>>>>> 44be761a363109ed6c6477fc87e0b2242688cd25
    private String address;
}
