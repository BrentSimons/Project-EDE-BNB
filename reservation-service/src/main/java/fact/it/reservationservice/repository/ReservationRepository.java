package fact.it.reservationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fact.it.reservationservice.model.Reservation;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT r FROM Reservation r
            WHERE r.roomId = :roomId
            AND (:startDate BETWEEN r.startDate AND r.endDate
            OR :endDate BETWEEN r.startDate AND r.endDate
            OR (r.startDate BETWEEN :startDate AND :endDate
            AND r.endDate BETWEEN :startDate AND :endDate))
            """)
    List<Reservation> findOverlappingReservations(
            @Param("roomId") String roomId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );
}
