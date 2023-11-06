package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.model.Movie;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable UUID id) {
        return new ResponseEntity<>(movieService.getMovie(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getMovies() {
        return new ResponseEntity<>(movieService.getMovies(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.addMovie(movie), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> changeTitle(@PathVariable UUID id, @RequestBody String title) {
        return new ResponseEntity<>(movieService.changeTitle(id, title), HttpStatus.OK);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Movie> changeCost(@PathVariable UUID id, @RequestBody double cost) {
//        return new ResponseEntity<>(movieService.changeCost(id, cost), HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMovie(@PathVariable UUID id) {
        return new ResponseEntity<>(movieService.deleteMovie(id), HttpStatus.OK);
    }
}
