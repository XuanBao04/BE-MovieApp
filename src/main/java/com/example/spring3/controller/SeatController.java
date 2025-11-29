package com.example.spring3.controller;

import com.example.spring3.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring3.dto.response.SeatResponse;
import com.example.spring3.service.SeatService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/seat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatController {
    SeatService seatService;

    @GetMapping("/{showtimeId}")
    public ApiResponse<List<SeatResponse>> getMethodName(@PathVariable String showtimeId) {

        return ApiResponse.<List<SeatResponse>>builder()
                .code(200)
                .result(seatService.getSeatByShowtimeId(showtimeId))
                .build();
    }

}
