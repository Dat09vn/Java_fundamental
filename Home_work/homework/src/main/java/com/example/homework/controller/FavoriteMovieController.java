package com.example.homework.controller;

import com.example.homework.model.Movie;
import com.example.homework.service.FavoriteMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FavoriteMovieController {

    private final FavoriteMovieService favoriteMovieService;

    @PostMapping("/{userId}/favorites")
    public void addFavoriteMovie(@PathVariable String userId, @RequestBody Movie movie) {
        favoriteMovieService.addFavoriteMovie(userId, movie);
    }

    @GetMapping("/{userId}/favorites")
    public List<Movie> listFavoriteMovies(@PathVariable String userId) {
        return favoriteMovieService.listFavoriteMovies(userId);
    }

    @DeleteMapping("/{userId}/favorites/{movieId}")
    public boolean removeFavoriteMovie(@PathVariable String userId, @PathVariable Long movieId) {
        return favoriteMovieService.removeFavoriteMovie(userId, movieId);
    }
}
