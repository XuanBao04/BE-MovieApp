package com.example.spring3.repository;

import com.example.spring3.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findByReleaseDateBefore(LocalDate date);
}
