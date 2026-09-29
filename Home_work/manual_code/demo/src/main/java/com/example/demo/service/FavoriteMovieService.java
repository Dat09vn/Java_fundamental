package com.example.demo.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.model.Movie;

@Service
public class FavoriteMovieService {
    private final Map<Long, Set<Movie>> userFavorites = new HashMap<>();

    public FavoriteMovieService() {
        // Initialize with some sample data if needed
        // userFavorites.put(1L, Set.of(new Movie(1L, "Inception"), new Movie(2L, "Interstellar")));
        // userFavorites.put(2L, Set.of(new Movie(3L, "The Matrix"), new Movie(4L, "The Godfather")));
        // userFavorites.put(3L, Set.of(new Movie(5L, "Pulp Fiction"), new Movie(6L, "The Dark Knight")));
    }

    //get all favorite movies for all users
    public Map<Long, Set<Movie>> getAllFavoriteMovies() {
        return userFavorites;
    }

    public String addFavoriteMovie(Long userId, Movie movie) {
        System.out.println("FavoriteMovieService:addFavoriteMovie: Adding movie " + movie.getTitle() + " to favorites for user with ID: " + userId);
        userFavorites.computeIfAbsent(userId, k -> new HashSet<>()).add(movie);
        return "Movie " + movie.getTitle() + " added to favorites for user with ID " + userId;
    }

    public Set<Movie> getFavoriteMovies(Long userId) {
        return userFavorites.getOrDefault(userId, Set.of());
    }


}
