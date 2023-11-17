package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.EndDateException;
import p.lodz.pl.pas2.exceptions.MovieException;
import p.lodz.pl.pas2.exceptions.RentNotExistException;
import p.lodz.pl.pas2.exceptions.RentalStillOngoingException;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Request.RentRequest;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    public RentController(p.lodz.pl.pas2.services.RentService rentService, UserService userService, MovieService movieService) {
        this.rentService = rentService;
        this.userService = userService;
        this.movieService = movieService;
    }

    //todo przypadek nieprawidłowej daty np startDate=abcde
    @PostMapping
    public ResponseEntity<?> addRent(@Valid @RequestBody RentRequest rentRequest) {
        User user = userService.getUser(rentRequest.getClientID());
        Movie movie = movieService.getMovie(rentRequest.getMovieID());
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserMsg.USER_NOT_FOUND);
        if(movie == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MovieMsg.MOVIE_NOT_FOUND);
        if(!user.isActive()) return ResponseEntity.status(HttpStatus.LOCKED).body(UserMsg.USER_NOT_ACTIVE);
        Rent rent = new Rent(user, movie, rentRequest.getStartDate());
        try {
            Rent addedRent = rentService.addRent(rent);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedRent);
        } catch (EndDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MovieException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        }
    }

    @GetMapping("/current")
    public ResponseEntity<List<Rent>> getCurrentRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getCurrentRents());
    }

    @GetMapping("/past")
    public ResponseEntity<List<Rent>> getPastRents() {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.getPastRents());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteRent(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(rentService.deleteRent(id));
        } catch (RentNotExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RentalStillOngoingException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> endRent(@PathVariable UUID id, @RequestBody(required = false) Map<String, String> endDate) {
        LocalDate endDateParsed = LocalDate.parse(endDate.get("endDate"));
//        try {
//            endDateParsed = LocalDate.parse(endDate.get("endDate"));
//        } catch (DateTimeParseException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date");
//        }
        try {
            Rent updatedRent = rentService.setEndTime(id, endDateParsed);
            return ResponseEntity.status(HttpStatus.OK).body(updatedRent);
        } catch (EndDateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not valid due to validation error: ");
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<String> handleConstraintViolationException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect date format");

    }
}
