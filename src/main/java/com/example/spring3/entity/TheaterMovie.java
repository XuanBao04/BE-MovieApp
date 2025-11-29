package com.example.spring3.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "theater_movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TheaterMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "theater_movie_id")
    String theaterMovieId;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    Theater theater;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    Integer status;
}
