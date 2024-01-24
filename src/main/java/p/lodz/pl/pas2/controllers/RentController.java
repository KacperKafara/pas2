package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.Dto.RentDto.RentDto;
import p.lodz.pl.pas2.Dto.RentDto.RentDtoMapper;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotForClientException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotFoundException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentsNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.request.RentRequest;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.security.UserAuthProvider;
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
    private final RentDtoMapper rentDtoMapper;
    private final UserAuthProvider userAuthProvider;

    @Autowired
    public RentController(RentService rentService, UserService userService, MovieService movieService, RentDtoMapper rentDtoMapper, UserAuthProvider userAuthProvider) {
        this.rentService = rentService;
        this.userService = userService;
        this.movieService = movieService;
        this.rentDtoMapper = rentDtoMapper;
        this.userAuthProvider = userAuthProvider;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentDto> getRent(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentToRentDto(rentService.getRent(id)));
    }

    @PostMapping
    public ResponseEntity<RentDto> addRent(@Valid @RequestBody RentRequest rentRequest) {
        User user = userService.getUser(rentRequest.getClientID());
        if(!(user instanceof Client)) throw new RentNotForClientException(RentMsg.RENT_FOR_WRONG_USER);
        Rent rent = new Rent((Client) user,
                movieService.getMovie(rentRequest.getMovieID()),
                rentRequest.getStartDate());
        Rent addedRent = rentService.addRent(rent);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentDtoMapper.rentToRentDto(addedRent));
    }

    @GetMapping("/current")
    public ResponseEntity<List<RentDto>> getCurrentRents(@RequestParam(required = false) UUID clientId, @RequestParam(required = false) UUID movieId) {
        if (clientId != null && movieId!= null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (clientId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getCurrentRentsByClient(clientId)));
        }
        if (movieId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getCurrentRentsByMovie(movieId)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getCurrentRents()));
    }

    @GetMapping("/past")
    public ResponseEntity<List<RentDto>> getPastRents(@RequestParam(required = false) UUID clientId, @RequestParam(required = false) UUID movieId) {
        if (clientId != null && movieId != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (clientId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getPastRentsByClient(clientId)));
        }
        if (movieId != null){
            return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getPastRentsByMovie(movieId)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentsToRentsDto(rentService.getPastRents()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRent(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(rentService.deleteRent(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RentDto> endRent(@PathVariable UUID id, @RequestBody(required = false) Map<String, String> endDate, @RequestHeader(value = "Authorization", required = false) String token) {
        User user = userAuthProvider.getUser(token);
        Rent rent = rentService.getRent(id);
        if (user instanceof Client && rent.getUser().getId() != user.getId()) {
            throw new RentNotForClientException(RentMsg.RENT_FOR_WRONG_USER);
        }
        LocalDate endDateParsed;
        try {
            endDateParsed = LocalDate.parse(endDate.get("endDate"));
        } catch (NullPointerException ignored) {
            endDateParsed = LocalDate.now();
        }
        Rent updatedRent = rentService.setEndTime(id, endDateParsed);
        return ResponseEntity.status(HttpStatus.OK).body(rentDtoMapper.rentToRentDto(updatedRent));
    }

    @ExceptionHandler(RentNotFoundException.class)
    ResponseEntity<Rent> handleRentNotFoundException(RentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
