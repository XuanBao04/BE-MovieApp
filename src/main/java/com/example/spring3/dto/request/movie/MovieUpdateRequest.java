package com.example.spring3.dto.request.movie;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieUpdateRequest {
    String cast;
    String description;
    String director;
    String duration;
    String genre;
//    String posterUrl;
    MultipartFile posterFile;
    LocalDate releaseDate;
    String title;
}
