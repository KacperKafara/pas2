package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.UserNotFound;
import p.lodz.pl.pas2.exceptions.UsernameInUse;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> getClientById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/{pattern}")
    public ResponseEntity<List<User>> getUsers(@PathVariable String pattern) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersByPattern(pattern));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByNickname(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username));
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
        } catch (UsernameInUse e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.setActive(id, Boolean.parseBoolean(active.get("active").toString())));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id,@Valid @RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}