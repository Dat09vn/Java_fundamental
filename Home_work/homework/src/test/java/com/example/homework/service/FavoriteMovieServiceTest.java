package com.example.homework.service;

import com.example.homework.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FavoriteMovieServiceTest {

    private FavoriteMovieService favoriteMovieService;

    @BeforeEach
    void setUp() {
        favoriteMovieService = new FavoriteMovieService();
    }

    @Test
    void shouldAddAndListFavoriteMoviesForUser() {
        Movie movie = new Movie(1L, "Inception");

        favoriteMovieService.addFavoriteMovie("user-1", movie);
        List<Movie> result = favoriteMovieService.listFavoriteMovies("user-1");

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
    }

    @Test
    void shouldRemoveFavoriteMovieById() {
        favoriteMovieService.addFavoriteMovie("user-1", new Movie(1L, "Inception"));
        favoriteMovieService.addFavoriteMovie("user-1", new Movie(2L, "Interstellar"));

        boolean removed = favoriteMovieService.removeFavoriteMovie("user-1", 1L);
        List<Movie> result = favoriteMovieService.listFavoriteMovies("user-1");

        assertTrue(removed);
        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getTitle());
    }

    @Test
    void shouldIgnoreDuplicateFavoriteMovie() {
        Movie movie = new Movie(1L, "Inception");

        favoriteMovieService.addFavoriteMovie("user-1", movie);
        favoriteMovieService.addFavoriteMovie("user-1", movie);

        List<Movie> result = favoriteMovieService.listFavoriteMovies("user-1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldBeThreadSafeForConcurrentFavoriteAdds() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch start = new CountDownLatch(1);

        try {
            Future<?> future1 = executorService.submit(() -> {
                await(start);
                favoriteMovieService.addFavoriteMovie("user-1", new Movie(1L, "Inception"));
            });
            Future<?> future2 = executorService.submit(() -> {
                await(start);
                favoriteMovieService.addFavoriteMovie("user-1", new Movie(2L, "Interstellar"));
            });
            Future<?> future3 = executorService.submit(() -> {
                await(start);
                favoriteMovieService.addFavoriteMovie("user-1", new Movie(3L, "Dune"));
            });
            Future<?> future4 = executorService.submit(() -> {
                await(start);
                favoriteMovieService.addFavoriteMovie("user-1", new Movie(4L, "Avatar"));
            });

            start.countDown();
            future1.get();
            future2.get();
            future3.get();
            future4.get();

            List<Movie> result = favoriteMovieService.listFavoriteMovies("user-1");
            assertEquals(4, result.size());
        } finally {
            executorService.shutdownNow();
        }
    }

    @Test
    void shouldReturnFalseWhenRemovingMissingMovie() {
        boolean removed = favoriteMovieService.removeFavoriteMovie("user-1", 99L);

        assertFalse(removed);
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
