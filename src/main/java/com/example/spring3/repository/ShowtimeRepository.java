package com.example.spring3.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring3.entity.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, String> {

  // Lấy tất cả suất chiếu dự vào movie id
  List<Showtime> findByMovie_MovieId(String movieId);

  // Lấy showtimes theo room
  @Query("""
          SELECT s FROM Showtime s
          WHERE s.movie.movieId = :movieId
            AND DATE(s.startTime) = :date
            AND s.room.roomId = :roomId
          ORDER BY s.startTime
      """)
  List<Showtime> getShowtimesByMovieDateAndRoom(
      @Param("movieId") String movieId,
      @Param("date") LocalDate date,
      @Param("roomId") String roomId);

  // Lấy tất cả ngày chiếu của phim
  @Query("""
      SELECT DISTINCT DATE(s.startTime)
      FROM Showtime s
      WHERE s.movie.movieId = :movieId
      ORDER BY DATE(s.startTime)
      """)
  List<java.sql.Date> getShowDatesByMovie(String movieId);

  // Lấy suất chiếu theo phim + ngày
  @Query("""
      SELECT s
      FROM Showtime s
      WHERE s.movie.movieId = :movieId
        AND DATE(s.startTime) = :date
      ORDER BY s.startTime
      """)
  List<Showtime> findShowtimesByMovieAndDate(
      @Param("movieId") String movieId,
      @Param("date") LocalDate date);

  // Lấy suất chiếu theo phim + ngày + rạp (dùng cho FE để show theo từng rạp)
  @Query("""
      SELECT s
      FROM Showtime s
      JOIN Room r ON r.roomId = s.room.roomId
      WHERE s.movie.movieId = :movieId
        AND DATE(s.startTime) = :date
        AND r.theater.theaterId = :theaterId
      ORDER BY s.startTime
      """)
  List<Showtime> findShowtimesByMovieAndDateAndTheater(
      @Param("movieId") String movieId,
      @Param("date") LocalDate date,
      @Param("theaterId") String theaterId);

  @Query("""
      SELECT DISTINCT r.theater.theaterId
      FROM Showtime s
      JOIN Room r ON r.roomId = s.room.roomId
      WHERE s.movie.movieId = :movieId
        AND DATE(s.startTime) = :date
      """)
  List<String> getTheaterIdsByMovieAndDate(
      @Param("movieId") String movieId,
      @Param("date") LocalDate date);

}
