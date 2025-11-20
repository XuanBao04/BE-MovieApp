package com.example.spring3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring3.entity.Seat;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

        // Lấy danh sách ghế + trạng thái theo showtime.
        @Query("""
                        SELECT s
                        FROM Seat s
                        WHERE s.room.id = :roomId
                        ORDER BY s.seatRow, s.seatNumber
                        """)
        List<Seat> findByRoom(@Param("roomId") String roomId);
}
