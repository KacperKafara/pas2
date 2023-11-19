package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
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
    public ResponseEntity<User> addUser(@Valid @RequestBody Moderator user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<User> setActive(@PathVariable UUID id, @RequestBody Map<String, Boolean> active) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.setActive(id, Boolean.parseBoolean(active.get("active").toString())));
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody Moderator user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, user));
    }
}
