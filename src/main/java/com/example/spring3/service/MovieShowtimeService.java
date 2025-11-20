package com.example.spring3.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring3.dto.response.MovieShowDatesResponse;
import com.example.spring3.dto.response.MovieShowtimesResponse;
import com.example.spring3.dto.response.RoomShowtimeResponse;
import com.example.spring3.dto.response.ShowtimeResponse;
import com.example.spring3.dto.response.TheaterShowtimeResponse;
import com.example.spring3.entity.Room;
import com.example.spring3.entity.Showtime;
import com.example.spring3.entity.Theater;
import com.example.spring3.repository.MovieRepository;
import com.example.spring3.repository.RoomRepository;
import com.example.spring3.repository.ShowtimeRepository;
import com.example.spring3.repository.TheaterRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieShowtimeService {

        MovieRepository movieRepository;
        ShowtimeRepository showtimeRepository;
        TheaterRepository theaterRepository;
        RoomRepository roomRepository;

        // 1) Lấy danh sách ngày chiếu của phim
        public MovieShowDatesResponse getShowDates(String movieId) {

                // Kiểm tra movie có tồn tại không
                movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Movie not found"));

                // Lấy danh sách ngày dạng java.sql.Date
                List<java.sql.Date> dateResults = showtimeRepository.getShowDatesByMovie(movieId);

                // Convert sang LocalDate
                List<LocalDate> dates = dateResults.stream()
                                .map(java.sql.Date::toLocalDate)
                                .toList();

                return MovieShowDatesResponse.builder()
                                .movieId(movieId)
                                .showDates(dates)
                                .build();
        }

        // 2. Lấy danh sách các suất chiếu (showtimes) của một phim, trong một ngày, tại
        // một rạp, sắp theo giờ chiếu
        public MovieShowtimesResponse getShowtimesByMovieAndDate(String movieId, LocalDate date) {

                // Validate movie
                movieRepository.findById(movieId)
                                .orElseThrow(() -> new RuntimeException("Movie not found"));

                // Lấy tất cả theater có suất chiếu ngày đó
                List<String> theaterIds = showtimeRepository.getTheaterIdsByMovieAndDate(movieId, date);

                List<TheaterShowtimeResponse> theaters = new ArrayList<>();

                for (String theaterId : theaterIds) {

                        var theater = theaterRepository.findById(theaterId)
                                        .orElseThrow(() -> new RuntimeException("Theater not found"));

                        // ⬇ Lấy rooms có suất chiếu
                        List<Room> rooms = roomRepository.findRoomsForMovieAndDate(movieId, date, theaterId);

                        List<RoomShowtimeResponse> roomResponses = new ArrayList<>();

                        for (Room room : rooms) {

                                // ⬇ Lấy showtimes theo phòng
                                var showtimes = showtimeRepository.getShowtimesByMovieDateAndRoom(
                                                movieId, date, room.getRoomId());

                                // ⬇ Map showtimes → ShowtimeResponse
                                List<ShowtimeResponse> showtimeResponses = showtimes.stream()
                                                .map(s -> new ShowtimeResponse(
                                                                s.getShowtimeId(),
                                                                s.getStartTime(),
                                                                s.getPrice()))
                                                .toList();

                                // ⬇ Map room → RoomShowtimeResponse
                                roomResponses.add(new RoomShowtimeResponse(
                                                room.getRoomId(),
                                                room.getType(),
                                                showtimeResponses));
                        }

                        // ⬇ Map theater → TheaterShowtimeResponse
                        theaters.add(TheaterShowtimeResponse.builder()
                                        .theater_id(theaterId)
                                        .theater_name(theater.getName())
                                        .rooms(roomResponses)
                                        .build());
                }

                // ➜ Final return
                return MovieShowtimesResponse.builder()
                                .movieId(movieId)
                                .showDate(date)
                                .theaters(theaters)
                                .build();
        }

}
