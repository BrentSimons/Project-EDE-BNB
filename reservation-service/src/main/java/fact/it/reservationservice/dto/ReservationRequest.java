package fact.it.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long personId;
    private Long roomId;
    private Date startDate;
    private Date endDate;
}
