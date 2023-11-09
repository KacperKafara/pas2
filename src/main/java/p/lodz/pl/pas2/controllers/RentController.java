package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Request.RentRequest;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
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
        if(user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with this id does not exist");
        if(movie == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with this id does not exist");
        if(!user.isActive()) return ResponseEntity.status(HttpStatus.LOCKED).body("User is not active");
        Rent rent = new Rent(user, movie, rentRequest.getStartDate());
        Rent addedRent = rentService.addRent(rent);
        if(addedRent == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie already rented or date is incorrect");
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRent);
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
    public ResponseEntity<Boolean> deleteRent(@PathVariable UUID id) {
        boolean deleteStatus = rentService.deleteRent(id);
        if(!deleteStatus) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> endRent(@PathVariable UUID id, @RequestBody(required = false) Map<String, String> endDate) {
        LocalDate endDateParsed;
        try {
            endDateParsed = LocalDate.parse(endDate.get("endDate"));
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date");
        }
//        if(endTime == null){
//            updatedRent = rentService.setEndTime(id, LocalDate.now());
//            return ResponseEntity.status(HttpStatus.OK).body(updatedRent);
//        }
        Rent updatedRent = rentService.setEndTime(id, endDateParsed);
        if(updatedRent == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRent);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
