package com.example.spring3.service;

import com.example.spring3.dto.request.movie.MovieCreateRequest;
import com.example.spring3.dto.request.movie.MovieUpdateRequest;
import com.example.spring3.dto.response.MovieResponse;
import com.example.spring3.entity.Movie;
import com.example.spring3.exception.AppException;
import com.example.spring3.exception.ErrorCode;
import com.example.spring3.mapper.MovieMapper;
import com.example.spring3.repository.MovieRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class MovieService {
    MovieRepository movieRepository;
    MovieMapper movieMapper;

    // lấy all movies
//    @PreAuthorize("hasRole('ADMIN')")
    public List<MovieResponse> getMovies() {
        var movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }
 // lấy danh sách movies sắp chiếu (movie trên ui người dùng)
    public List<MovieResponse> getMoviesforUser() {
        var movies = movieRepository.findByReleaseDateBefore(LocalDate.now());
        return movies.stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }


    // lấy 1 movie theo id
    public MovieResponse getMovie(String id){
        return  movieMapper.toMovieResponse(movieRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_EXIST)));
    }

    // tạo movie
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse createMovie(MovieCreateRequest request){

        Movie movie = movieMapper.toMovie(request);
         movie = movieRepository.save(movie);
        return movieMapper.toMovieResponse(movie);
    }


    // update thông tin movie
    @PreAuthorize("hasRole('ADMIN')")
    public MovieResponse updateMovie(String movieId, MovieUpdateRequest request){
       Movie movie = movieRepository.findById(movieId)
               .orElseThrow(()->new RuntimeException("movie not found"));
       movieMapper.updateMovie(movie,request);

        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }
    @PreAuthorize("hasRole('ADMIN')")
    // xóa movie
    public void deleteMovie(String movieId){
        movieRepository.deleteById(movieId);
    }


}
