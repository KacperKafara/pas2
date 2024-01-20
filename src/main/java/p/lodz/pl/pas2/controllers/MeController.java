package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.Dto.RentDto.RentDto;
import p.lodz.pl.pas2.Dto.RentDto.RentDtoMapper;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentForAnotherClientException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotForClientException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.request.RentRequest;
import p.lodz.pl.pas2.security.UserAuthProvider;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final UserService userService;
    private final MovieService movieService;
    private final RentService rentService;
    private final RentDtoMapper rentDtoMapper;
    private final UserAuthProvider userAuthProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MeController(UserService userService,
                        MovieService movieService,
                        RentService rentService,
                        RentDtoMapper rentDtoMapper,
                        UserAuthProvider userAuthProvider,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.movieService = movieService;
        this.rentService = rentService;
        this.rentDtoMapper = rentDtoMapper;
        this.userAuthProvider = userAuthProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PatchMapping("/password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> body) {
        User user = userAuthProvider.getUser(token);
        String newPassword = body.get("password");
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user.getId(), user);
        return ResponseEntity.ok("Password changed");
    }

    @PostMapping("/rent")
    public ResponseEntity<RentDto> addRent(@RequestHeader("Authorization") String token, @RequestBody RentRequest rentRequest) {
        User user = userAuthProvider.getUser(token);
        if(rentRequest.getClientID() != user.getId()) throw new RentForAnotherClientException(RentMsg.RENT_FOR_OTHER_CLIENT);
        if(!(user instanceof Client)) throw new RentNotForClientException(RentMsg.RENT_FOR_WRONG_USER);
        Rent rent = new Rent((Client) user,
                movieService.getMovie(rentRequest.getMovieID()),
                rentRequest.getStartDate());
        Rent addedRent = rentService.addRent(rent);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentDtoMapper.rentToRentDto(addedRent));
    }
}
