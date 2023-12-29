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
    /*@Query("""
            SELECT r FROM Reservation r
            WHERE r.roomCode = :roomCode
            AND (:startDate BETWEEN r.startDate AND r.endDate
            OR :endDate BETWEEN r.startDate AND r.endDate
            OR (r.startDate BETWEEN :startDate AND :endDate
            AND r.endDate BETWEEN :startDate AND :endDate))
            """)
    List<Reservation> findOverlappingReservations(
            @Param("roomCode") String roomCode,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );*/

    List<Reservation> findByRoomCode(String roomCodes);

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.roomCode = :roomCode
            AND r.startDate BETWEEN CURRENT_DATE AND :nextMonthStartDate
            """)
    List<Reservation> findByRoomCodeAndStartDateInNextMonthOrderByStartDateAsc(
            @Param("roomCode") String roomCode,
            @Param("nextMonthStartDate") Date nextMonthStartDate
    );
}
