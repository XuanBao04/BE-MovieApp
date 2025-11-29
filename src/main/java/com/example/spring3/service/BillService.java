package com.example.spring3.service;


import com.example.spring3.dto.response.ShowtimeResponse;
import com.example.spring3.entity.Showtime;
import com.example.spring3.mapper.ShowtimeMapper;
import com.example.spring3.repository.BillRepository;
import com.example.spring3.repository.ShowtimeRepository;
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
public class BillService
{
    ShowtimeRepository showtimeRepository;
    BillRepository billRepository;
    ShowtimeMapper showtimeMapper;

    public List<ShowtimeResponse> getShowtimeByMovieId(String movieId) {

        List<Showtime> showtimes = showtimeRepository.findByMovie_MovieId(movieId);
        if (showtimes.isEmpty()) {
            throw new RuntimeException("No showtime found");
        }

        return showtimeMapper.toShowtimeResponseList(showtimes);
    }
}
