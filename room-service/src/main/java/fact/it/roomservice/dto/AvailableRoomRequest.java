package fact.it.roomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomRequest {
    // TODO roomIds only needed if we want to filter for a certain bnb, otherwise we can filter through all rooms and check availability
    private List<String> roomIds;
    private Date startDate;
    private Date endDate;
    private int size;
}
