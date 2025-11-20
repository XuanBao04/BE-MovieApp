package com.example.spring3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.spring3.entity.Movie;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {

}
