package fact.it.roomservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "room")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Room {
    private String id;
    // private String roomCode;
    private String name;
    private int size;
}
