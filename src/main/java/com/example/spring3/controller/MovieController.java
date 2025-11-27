package com.example.spring3.controller;

import com.example.spring3.dto.response.ApiResponse;
import com.example.spring3.dto.request.movie.MovieCreateRequest;
import com.example.spring3.dto.request.movie.MovieUpdateRequest;
import com.example.spring3.dto.response.MovieResponse;
import com.example.spring3.repository.MovieRepository;
import com.example.spring3.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    @Autowired
    private MovieRepository repository;
    private final MovieService movieService;

    // Get all movies
    @GetMapping
    public ApiResponse<List<MovieResponse>> getAll() {
        return ApiResponse.<List<MovieResponse>>builder()
                .result(movieService.getMovies())
                .build();
    }

    // Get movie by id
    @GetMapping("/{movieId}")
    public ApiResponse<MovieResponse> getById(@PathVariable String movieId) {
        MovieResponse response = movieService.getMovie(movieId);
        return ApiResponse.<MovieResponse>builder()
                .result(response)
                .build();
    }
// create movie
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Chỉ định nhận form-data
    public ApiResponse<MovieResponse> createMovie(@ModelAttribute MovieCreateRequest request) {
        // @ModelAttribute sẽ tự động map các field trong form và file vào DTO
        MovieResponse result = movieService.createMovie(request);
        return ApiResponse.<MovieResponse>builder()
                .result(result)
                .build();
    }

    // Update movie
    @PutMapping(value = "/{movieId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MovieResponse> updateMovie(@PathVariable("movieId") String movieId,
                                                  @ModelAttribute @Valid MovieUpdateRequest request) {
        return ApiResponse.<MovieResponse>builder()
                .result(movieService.updateMovie(movieId, request))
                .build();
    }

    // Delete movie
    @DeleteMapping("/{movieId}")
    String deleteMovie(@PathVariable("movieId") String movieId){
        movieService.deleteMovie(movieId);
        return "Movie had been deleted";
    }
}
