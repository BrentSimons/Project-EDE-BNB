package fact.it.bnbservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="bnb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bnb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ElementCollection
    private List<Long> roomsIdList;

    private String city;
    private String postcode;
    private String address;

    // Add a room ID to list
    public void addRoomId(Long roomId) {
        if (roomsIdList == null) { roomsIdList = new ArrayList<>(); }
        roomsIdList.add(roomId);
    }
}
