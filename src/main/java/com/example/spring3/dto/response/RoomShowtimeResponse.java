package com.example.spring3.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomShowtimeResponse {
    private String room_id;
    private String room_type;
    private List<ShowtimeResponse> showtimes;
}
