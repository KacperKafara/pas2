package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rent")
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
        return new ResponseEntity<>(rentService.getRent(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Rent>> getRents() {
        return new ResponseEntity<>(rentService.getRents(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Rent> addRent() {
        return null;
    }
}
