package com.example.spring3.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {
     String movieId;
     String cast;
     String description;
     String director;
     String duration;
     String genre;
     String posterUrl;
     LocalDate releaseDate;
     String title;
}
