package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.services.MovieService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(p.lodz.pl.pas2.services.MovieService movieService) {
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
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        Movie addedMovie = movieService.addMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedMovie);
    }

    @PutMapping(value = "/id/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable UUID id, @Valid @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        if(updatedMovie == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MovieMsg.MOVIE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        boolean deleteStatus = movieService.deleteMovie(id);
        if(!deleteStatus) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MovieMsg.MOVIE_CANNOT_BE_REMOVED);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
