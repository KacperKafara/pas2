package p.lodz.pl.pas2.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import p.lodz.pl.pas2.exceptions.movieExceptions.*;
import p.lodz.pl.pas2.exceptions.rentExceptions.*;
import p.lodz.pl.pas2.exceptions.userExceptions.ThereIsNoUserToUpdateException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotActiveException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("not valid due to validation error: " + e.getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<String> handleConstraintViolationException(DateTimeParseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect date format");
    }

    @ExceptionHandler(MovieNotFoundException.class)
    ResponseEntity<Movie> handleMovieNotFoundException(MovieNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ExceptionHandler(MoviesNotFoundException.class)
    ResponseEntity<List<Movie>> handleMoviesNotFoundException(MoviesNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
    }

    @ExceptionHandler(MovieInUseException.class)
    ResponseEntity<String> handleMovieInUseException(MovieInUseException e) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
    }

    @ExceptionHandler(ThereIsNoSuchMovieToUpdateException.class)
    ResponseEntity<String> handleThereIsNoSuchMovieToUpdateException(ThereIsNoSuchMovieToUpdateException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ThereIsNoSuchRentToDelete.class)
    ResponseEntity<String> handleThereIsNoSuchRentToDeleteException(ThereIsNoSuchRentToDelete e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EndDateException.class)
    ResponseEntity<String> handleEndDateException(EndDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler(StartDateException.class)
    ResponseEntity<String> handleEndDateException(StartDateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RentsNotFoundException.class)
    ResponseEntity<List<Rent>> handleRentsNotFoundException(RentsNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
    }

    @ExceptionHandler(RentNotFoundException.class)
    ResponseEntity<String> handleRentNotFoundException(RentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RentalStillOngoingException.class)
    ResponseEntity<String> handleRentalStillOngoingException(RentalStillOngoingException e) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    @ExceptionHandler(UserNotActiveException.class)
    ResponseEntity<String> handleUserNotActiveException(UserNotActiveException e) {
        return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
    }

    @ExceptionHandler(UsernameInUseException.class)
    ResponseEntity<String> handleUsernameInUseException(UsernameInUseException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ThereIsNoUserToUpdateException.class)
    ResponseEntity<String> handleThereIsNoUserToUpdateException(ThereIsNoUserToUpdateException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ThereIsNoSuchRentToUpdateException.class)
    ResponseEntity<String> handleThereIsNoSuchRentToUpdateException(ThereIsNoSuchRentToUpdateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ThereIsNoSuchMovieToDeleteException.class)
    ResponseEntity<String> handleThereIsNoSuchMovieToDeleteException(ThereIsNoSuchMovieToDeleteException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RentNotForClientException.class)
    ResponseEntity<String> handleRentNotForClientException(RentNotForClientException e) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
