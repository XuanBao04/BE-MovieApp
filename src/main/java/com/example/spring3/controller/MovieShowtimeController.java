package com.example.spring3.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.spring3.dto.response.ApiResponse;
import com.example.spring3.dto.response.MovieShowDatesResponse;
import com.example.spring3.dto.response.MovieShowtimesResponse;
import com.example.spring3.service.MovieShowtimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/movies")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieShowtimeController {
    MovieShowtimeService movieShowtimeService;

    @GetMapping("/{movieId}/show-dates")
    public ApiResponse<MovieShowDatesResponse> getShowDates(@PathVariable String movieId) {
        return ApiResponse.<MovieShowDatesResponse>builder()
                .result(movieShowtimeService.getShowDates(movieId))
                .build();
    }

    @GetMapping("/{movieId}/showtimes")
    public ResponseEntity<MovieShowtimesResponse> getShowtimesByMovieAndDate(
            @PathVariable String movieId,
            @RequestParam LocalDate date) {
        MovieShowtimesResponse response = movieShowtimeService
                .getShowtimesByMovieAndDate(movieId, date);

        return ResponseEntity.ok(response);
    }

}
