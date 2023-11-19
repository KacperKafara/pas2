package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieNotFoundException;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieInUseException;
import p.lodz.pl.pas2.exceptions.rentExceptions.EndDateException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotFoundException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentalStillOngoingException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentsNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotActiveException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.request.RentRequest;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    //todo przypadek nieprawidłowej daty np startDate=abcde
    @PostMapping
    public ResponseEntity<Rent> addRent(@Valid @RequestBody RentRequest rentRequest) {
        Rent rent = new Rent(userService.getUser(rentRequest.getClientID()),
                movieService.getMovie(rentRequest.getMovieID()),
                rentRequest.getStartDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(rentService.addRent(rent));
    }

    @GetMapping("/current")
    public ResponseEntity<List<Rent>> getCurrentRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getCurrentRents());
    }

    @GetMapping("/past")
    public ResponseEntity<List<Rent>> getPastRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getPastRents());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRent(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.deleteRent(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Rent> endRent(@PathVariable UUID id, @RequestBody(required = false) Map<String, String> endDate) {
        LocalDate endDateParsed = LocalDate.parse(endDate.get("endDate"));
        Rent updatedRent = rentService.setEndTime(id, endDateParsed);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRent);
    }
}
