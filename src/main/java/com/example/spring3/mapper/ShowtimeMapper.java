package com.example.spring3.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.spring3.dto.response.ShowtimeResponse;
import com.example.spring3.entity.Showtime;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {

    ShowtimeResponse toShowtimeResponse(Showtime showtime);

    List<ShowtimeResponse> toShowtimeResponseList(List<Showtime> showtimes);

}
