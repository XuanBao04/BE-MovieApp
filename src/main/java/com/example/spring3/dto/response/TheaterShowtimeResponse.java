package com.example.spring3.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TheaterShowtimeResponse {
    String theater_id;
    String theater_name;
    String theater_address;
    // List<ShowtimeResponse> showtimes;
    List<RoomShowtimeResponse> rooms;
}
