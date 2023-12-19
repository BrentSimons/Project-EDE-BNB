package fact.it.bnbservice.model;

import jakarta.persistence.*;
import lombok.*;

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
    private List<Long> RoomsIdList;
}
