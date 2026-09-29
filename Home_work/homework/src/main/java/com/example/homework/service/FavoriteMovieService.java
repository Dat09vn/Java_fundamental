package com.example.homework.service;

import com.example.homework.model.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class FavoriteMovieService {

    private final Map<String, Set<Movie>> favoriteMoviesByUser = new ConcurrentHashMap<>();

    public void addFavoriteMovie(String userId, Movie movie) {
        favoriteMoviesByUser.computeIfAbsent(userId, ignored -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(movie);
    }

    public List<Movie> listFavoriteMovies(String userId) {
        return new ArrayList<>(favoriteMoviesByUser.getOrDefault(userId, Collections.emptySet()));
    }

    public boolean removeFavoriteMovie(String userId, Long movieId) {
        Set<Movie> movies = favoriteMoviesByUser.get(userId);
        if (movies == null || movies.isEmpty()) {
            return false;
        }

        return movies.removeIf(movie -> movie.getId().equals(movieId));
    }
}
