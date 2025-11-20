package com.example.spring3.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring3.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

        @Query("""
                        SELECT DISTINCT r
                        FROM Room r
                        JOIN Showtime s ON s.room.roomId = r.roomId
                        WHERE s.movie.movieId = :movieId
                                AND FUNCTION('DATE', s.startTime) = :date
                                AND r.theater.theaterId = :theaterId
                        ORDER BY r.name
                        """)
        List<Room> findRoomsForMovieAndDate(
                        @Param("movieId") String movieId,
                        @Param("date") LocalDate date,
                        @Param("theaterId") String theaterId);

}
