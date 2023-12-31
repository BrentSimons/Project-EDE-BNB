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

    private Long id;

    private String name;

    private List<String> roomCodes;

    private String city;

    private String postcode;

    private String address;
}
