package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieInUseException;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieNotFoundException;
import p.lodz.pl.pas2.exceptions.movieExceptions.MoviesNotFoundException;
import p.lodz.pl.pas2.exceptions.movieExceptions.ThereIsNoSuchMovieToUpdateException;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.request.MovieRequest;
import p.lodz.pl.pas2.services.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovie(id));
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovies());
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieRequest movie) {
        Movie addedMovie = movieService.addMovie(new Movie(movie.getTitle(), movie.getCost()));
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovie);
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable UUID id, @Valid @RequestBody MovieRequest movie) {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.updateMovie(id, new Movie(movie.getTitle(), movie.getCost())));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Boolean> deleteMovie(@PathVariable UUID id) {
        boolean deleteStatus = movieService.deleteMovie(id);
        if(!deleteStatus) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
