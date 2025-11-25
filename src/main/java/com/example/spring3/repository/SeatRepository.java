package com.example.spring3.repository;

import java.util.List;

import com.example.spring3.dto.response.SeatResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring3.entity.Seat;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

    @Query("""
    SELECT new com.example.spring3.dto.response.SeatResponse(
        s.seatId,
        s.seatRow,
        s.seatNumber,
        s.seatType,
        CASE WHEN t.ticketId IS NOT NULL THEN 1 ELSE 0 END
    )
    FROM Seat s
    JOIN s.room r
    JOIN Showtime st ON st.room.roomId = r.roomId
    LEFT JOIN Ticket t ON t.seat.seatId = s.seatId
        AND t.showtime.showtimeId = st.showtimeId
        AND t.status = 1
    WHERE st.showtimeId = :showtimeId
    ORDER BY s.seatRow, s.seatNumber
""")
    List<SeatResponse> getSeatStatusByShowtimeId(String showtimeId);

}
