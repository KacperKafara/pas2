package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.exceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.services.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/moderators")
public class ModeratorController {

    private final UserService userService;

    @Autowired
    public ModeratorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody Moderator user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
        } catch (UsernameInUseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.setActive(id, Boolean.parseBoolean(active.get("active").toString())));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id,@Valid @RequestBody Moderator user) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
