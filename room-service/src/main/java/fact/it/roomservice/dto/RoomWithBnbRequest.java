package fact.it.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomWithBnbRequest {
    private RoomRequest roomRequest;
    private Long bnbId;
}
