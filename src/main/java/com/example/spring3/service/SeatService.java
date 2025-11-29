package com.example.spring3.service;

import com.example.spring3.dto.response.SeatResponse;
import com.example.spring3.mapper.SeatMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SeatService {
    SeatRepository seatRepository;
    SeatMapper seatMapper;

    public List<SeatResponse> getSeatByShowtimeId(String showtimeId) {

        List<SeatResponse> seats = seatRepository.getSeatStatusByShowtimeId1(showtimeId);
        if (seats.isEmpty()) {
            throw new RuntimeException("No showtime found");
        }

        return seatMapper.toSeatResponseList(seats);
    }
}
