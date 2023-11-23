package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotFoundException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentsNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.request.RentRequest;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("api/v1/rents")
public class RentController {

    private final RentService rentService;
    private final UserService userService;
    private final MovieService movieService;

    @Autowired
    public RentController(RentService rentService, UserService userService, MovieService movieService) {
        this.rentService = rentService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rent> getRent(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getRent(id));
    }

    @PostMapping
    public ResponseEntity<Rent> addRent(@Valid @RequestBody RentRequest rentRequest) {
        Rent rent = new Rent(userService.getUser(rentRequest.getClientID()),
                movieService.getMovie(rentRequest.getMovieID()),
                rentRequest.getStartDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.addRent(rent));
    }

    @GetMapping("/current")
    public ResponseEntity<List<Rent>> getCurrentRents(@RequestParam(required = false) UUID clientId, @RequestParam(required = false) UUID movieId) {
        if (clientId != null && movieId!= null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (clientId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentService.getCurrentRentsByClient(clientId));
        }
        if (movieId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentService.getCurrentRentsByMovie(movieId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getCurrentRents());
    }

    @GetMapping("/past")
    public ResponseEntity<List<Rent>> getPastRents(@RequestParam(required = false) UUID clientId, @RequestParam(required = false) UUID movieId) {
        if (clientId != null && movieId != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (clientId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentService.getPastRentsByClient(clientId));
        }
        if (movieId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentService.getPastRentsByMovie(movieId));
        }
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getPastRents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRent(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.deleteRent(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rent> endRent(@PathVariable UUID id, @RequestBody(required = false) Map<String, String> endDate) {
        LocalDate endDateParsed;
        if(endDate != null) {
            endDateParsed = LocalDate.parse(endDate.get("endDate"));
        } else {
            endDateParsed = LocalDate.now();
        }
        Rent updatedRent = rentService.setEndTime(id, endDateParsed);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRent);
    }

    @ExceptionHandler(RentNotFoundException.class)
    ResponseEntity<Rent> handleRentNotFoundException(RentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
