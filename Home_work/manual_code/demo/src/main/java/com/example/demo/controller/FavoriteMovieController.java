package com.example.demo.controller;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Movie;
import com.example.demo.service.FavoriteMovieService;


@RestController
@RequestMapping()

public class FavoriteMovieController {

    private final FavoriteMovieService favoriteMovieService;

    public FavoriteMovieController(FavoriteMovieService favoriteMovieService) {
        this.favoriteMovieService = favoriteMovieService;
    }
    
    @GetMapping("/users/{userId}/favorites")
    public Set<Movie> getFavoriteMovies(@PathVariable("userId") Long userId) {
        System.out.println("FavoriteMovieController:getFavoriteMovies: Received request to get favorite movies for user with ID: " + userId);
        return favoriteMovieService.getFavoriteMovies(userId);
    }

    @GetMapping("/users/{userId}/favorites/")
    public Map<String, String> favorites() {
        return Map.of(
                "message", "Favorite Movie API is running",
                "endpoint", "/users/{userId}/favorites"
        );
    }

    @GetMapping("/allMovies")
    public Map<Long, Set<Movie>> getAllFavoriteMovies() {
        System.out.println("FavoriteMovieController:getAllFavoriteMovies: Received request to get all favorite movies for all users");
        return favoriteMovieService.getAllFavoriteMovies();
    }

    @PostMapping("/users/{userId}/favorites")
    public ResponseEntity<String> addFavoriteMovie(@PathVariable("userId") Long userId, @RequestBody Movie movie) {
        System.out.println("FavoriteMovieController:addFavoriteMovie: Received request to add favorite movie for user with ID: " + userId);
        return ResponseEntity.ok(favoriteMovieService.addFavoriteMovie(userId, movie));
    }   

}
