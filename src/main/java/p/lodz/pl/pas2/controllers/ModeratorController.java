package p.lodz.pl.pas2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.request.ModeratorRequest;
import p.lodz.pl.pas2.services.UserService;

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
    public ResponseEntity<User> addUser(@Valid @RequestBody ModeratorRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(new Moderator(user.getUsername(), user.isActive())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody ModeratorRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, new Moderator(user.getUsername(), user.isActive())));
    }
}