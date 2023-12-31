package fact.it.bnbservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bnb")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bnb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private String postcode;
    private String address;

    @ElementCollection
    private List<String> roomCodes = new ArrayList<>();

    // Add a room code to the list
    public void addRoomCode(String roomCode) {
        if (roomCodes == null) {
            roomCodes = new ArrayList<>();
        }
        roomCodes.add(roomCode);
    }
}

