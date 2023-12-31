package fact.it.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private String personId;
    private String roomCode;
    private LocalDate startDate;
    private LocalDate endDate;
}
