package p.lodz.pl.pas2.controllers;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.Dto.UserDto.UserDto;
import p.lodz.pl.pas2.Dto.UserDto.UserDtoMapper;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsersNotFoundException;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.security.Jws;
import p.lodz.pl.pas2.services.UserService;

import java.util.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;
    private final Jws jws;

    @Autowired
    public UserController(UserService userService, UserDtoMapper userDtoMapper, Jws jws) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.jws = jws;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getClientById(@PathVariable UUID id) throws JOSEException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("ETag", jws.generateSign(id));
        return ResponseEntity.ok()
                .headers(headers)
                .body(userDtoMapper.userToUserDto(userService.getUser(id)));
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String username) {
        if (Objects.nonNull(username)){
            try {
                User user = userService.getUser(username);
                return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.userToUserDto(user));
            } catch (UserNotFoundException e1) {
                List<User> users = userService.getUsersByPattern(username);
                return ResponseEntity.status(HttpStatus.OK).body(userDtoMapper.usersToUserDtos(users));
            }
        }
        List<UserDto> usersList = userDtoMapper.usersToUserDtos(userService.getUsers());
        return !usersList.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(usersList)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.setActive(id, Boolean.parseBoolean(active.get("active").toString())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<User> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ExceptionHandler(UsersNotFoundException.class)
    ResponseEntity<List<User>> handleUsersNotFoundException(UsersNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
    }
}