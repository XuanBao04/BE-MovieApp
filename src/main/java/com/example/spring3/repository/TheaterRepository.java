package com.example.spring3.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.spring3.entity.Theater;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, String> {

    // Lấy danh sách rạp chiếu một phim trong ngày (dựa vào theater_movies hoặc
    // showtimes).
    @Query("""
            SELECT DISTINCT t
            FROM Theater t
            JOIN Room r ON r.theater.id = t.id
            JOIN Showtime s ON s.room.id = r.id
            WHERE s.movie.id = :movieId
              AND DATE(s.startTime) = :date
            """)
    List<Theater> findTheatersByMovieAndDate(
            @Param("movieId") String movieId,
            @Param("date") LocalDate date);
}
