package p.lodz.pl.pas2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getClientById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String username) {
        if (Objects.nonNull(username)){
            try {
                User user = userService.getUser(username);
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } catch (UserNotFoundException e1){

                try {
                    List<User> users = userService.getUsersByPattern(username);
                    return ResponseEntity.status(HttpStatus.OK).body(users);
                }catch (UserNotFoundException ignored){

                }
            }
        }
        List<User> usersList = userService.getUsers();
        return usersList != null
                ? ResponseEntity.status(HttpStatus.OK).body(usersList)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no user with given username");
    }

//    @GetMapping("/{pattern}")
//    public ResponseEntity<List<User>> getUsers(@PathVariable String pattern) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersByPattern(pattern));
//    }

//    @GetMapping("/username/{username}")
//    public ResponseEntity<User> getUserByNickname(@PathVariable String username) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username));
//    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<User> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.setActive(id, Boolean.parseBoolean(active.get("active").toString())));
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }
}