package com.example.spring3.dto.response;

import com.example.spring3.entity.Seat;
import com.example.spring3.entity.Showtime;
import com.example.spring3.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
    String ticketId;
    Showtime showtime;
    Seat seat;
    User user;
    LocalDateTime bookingTime;
    LocalDateTime expireTime;
    Integer status;
}
