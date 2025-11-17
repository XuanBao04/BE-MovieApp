package com.example.spring3.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "movie_id", length = 50)
    String id;

    String title;

    String genre;

    int duration;

    String description;

    @Column(name = "release_date")
    LocalDate releaseDate;

    @Column(name = "poster_url")
    String posterUrl;

    String director;

    String cast;
}
