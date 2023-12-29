package fact.it.bnbservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomResponse {
    private String roomCode;
    private boolean isAvailable;
}
