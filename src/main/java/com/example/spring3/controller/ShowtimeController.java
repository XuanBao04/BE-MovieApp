package com.example.spring3.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring3.dto.response.ApiResponse;
import com.example.spring3.dto.response.ShowtimeResponse;
import com.example.spring3.service.ShowtimeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowtimeController {
    ShowtimeService showtimeService;

    @GetMapping("/{movieId}")
    public ApiResponse<List<ShowtimeResponse>> getMethodName(@PathVariable String movieId) {

        return ApiResponse.<List<ShowtimeResponse>>builder()
                .code(200)
                .result(showtimeService.getShowtimeByMovieId(movieId))
                .build();
    }
    @GetMapping
    public ApiResponse<List<ShowtimeResponse>> getAllShowtimes(){
        return ApiResponse.<List<ShowtimeResponse>>builder()
                .code(200)
                .result(showtimeService.getAllShowtimes())
                .build();
    }


}
