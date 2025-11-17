package com.example.spring3.dto.request.movie;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieCreateRequest {
    String cast;
    String description;
    String director;
    String duration;
    String genre;
    String posterUrl;
    LocalDate releaseDate;
    String title;
}
