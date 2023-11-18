package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.MovieException;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.request.MovieRequest;
import p.lodz.pl.pas2.msg.MovieMsg;
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
        try {
            return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovie(id));
        } catch (MovieException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovies());
        } catch (MovieException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
        }
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieRequest movie) {
        Movie addedMovie = movieService.addMovie(MovieRequest.movieRequestToMovie(movie));
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovie);
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable UUID id, @Valid @RequestBody MovieRequest movie) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(movieService.updateMovie(id, MovieRequest.movieRequestToMovie(movie)));
        } catch (MovieException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        try {
            boolean deleteStatus = movieService.deleteMovie(id);
            if(!deleteStatus) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (MovieException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(MovieMsg.MOVIE_CANNOT_BE_REMOVED);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
