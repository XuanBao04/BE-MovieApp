package com.example.spring3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring3.dto.response.ShowtimeResponse;
import com.example.spring3.entity.Showtime;
import com.example.spring3.mapper.ShowtimeMapper;
import com.example.spring3.repository.ShowtimeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ShowtimeService {
    ShowtimeRepository showtimeRepository;
    ShowtimeMapper showtimeMapper;

    public List<ShowtimeResponse> getShowtimeByMovieId(String movieId) {

        List<Showtime> showtimes = showtimeRepository.findByMovie_MovieId(movieId);
        if (showtimes.isEmpty()) {
            throw new RuntimeException("No showtime found");
        }

        return showtimeMapper.toShowtimeResponseList(showtimes);
    }
    public List<ShowtimeResponse> getAllShowtimes(){
        var showtimes = showtimeRepository.findAll();
        return showtimes.stream()
                .map(showtimeMapper::toShowtimeResponse)
                .toList();
    }

}
