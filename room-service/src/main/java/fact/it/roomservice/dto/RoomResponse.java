package fact.it.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    // TODO remove id line, also update builders in service (getAllRooms() and getRoom() ) to not add id
    private String id;
    private String roomCode;
    private String name;
    private int size;
}
