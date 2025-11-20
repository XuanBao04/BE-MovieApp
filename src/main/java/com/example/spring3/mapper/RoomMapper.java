package com.example.spring3.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.spring3.dto.response.RoomShowtimeResponse;
import com.example.spring3.entity.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomShowtimeResponse toRoomShowtimeResponse(Room room);

    // List<ShowtimeResponse> toShowtimeResponseList(List<Showtime> showtimes);

}
