package com.example.spring3.mapper;


import com.example.spring3.dto.request.movie.MovieCreateRequest;
import com.example.spring3.dto.request.movie.MovieUpdateRequest;
import com.example.spring3.dto.response.MovieResponse;
import com.example.spring3.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toMovie(MovieCreateRequest request);
    MovieResponse toMovieResponse(Movie movie);
    void updateMovie(@MappingTarget Movie movie, MovieUpdateRequest request);
}

